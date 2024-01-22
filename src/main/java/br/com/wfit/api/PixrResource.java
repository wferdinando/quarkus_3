package br.com.wfit.api;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import br.com.wfit.model.Chave;
import br.com.wfit.model.LinhaDigitavel;
import br.com.wfit.model.Pix;
import br.com.wfit.model.Transaction;
import br.com.wfit.service.DictService;
import br.com.wfit.service.PixService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/pix")
@Authenticated
public class PixrResource {

    @Inject
    DictService dictService;

    @Inject
    PixService pixService;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Operation(description = "API para criar uma linha digitável.")
    @APIResponseSchema(LinhaDigitavel.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK."),
            @APIResponse(responseCode = "201", description = "Retorno OK com a transação criada."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação da API."),
            @APIResponse(responseCode = "403", description = "Erro de autorização da API."),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado."),

    })

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/linha")
    public Response gerarLinhaDigitavel(final Pix pix) {

        Chave chave = dictService.buscarChave(pix.chave());

        if (Objects.nonNull(chave)) {
            return Response.ok(pixService.gerarLinhaDigitavel(chave, pix.valor(), pix.cidadeRemetente())).build();
        }

        return null;
    }

    @Operation(description = "API responsável por aprovar um pagamento PIX")
    @APIResponseSchema(Transaction.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK."),
            @APIResponse(responseCode = "201", description = "Retorno OK com a transação criada."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação da API."),
            @APIResponse(responseCode = "403", description = "Erro de autorização da API."),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado."),

    })
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{uuid}/aprovar")
    public Response aprovarPix(@PathParam("uuid") final String uuid) {
        return Response.ok(pixService.aprovarTransacao(uuid).get()).build();
    }

    @Operation(description = "API responsável por reprovar um pagamento PIX")
    @APIResponseSchema(Transaction.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK."),
            @APIResponse(responseCode = "201", description = "Retorno OK com a transação criada."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação da API."),
            @APIResponse(responseCode = "403", description = "Erro de autorização da API."),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado."),

    })
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{uuid}/reprovar")
    public Response reprovarPix(@PathParam("uuid") final String uuid) {
        return Response.ok(pixService.reprovarTransacao(uuid).get()).build();
    }

    @Operation(description = "API responsável por buscar um pagamento PIX")
    @APIResponseSchema(Transaction.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK."),
            @APIResponse(responseCode = "201", description = "Retorno OK com a transação criada."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação da API."),
            @APIResponse(responseCode = "403", description = "Erro de autorização da API."),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado."),

    })
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{uuid}")
    public Response buscarPix(@PathParam("uuid") final String uuid) {
        return Response.ok(pixService.findById(uuid)).build();
    }

    @Operation(description = "API responsável por buscar pagamentos PIX")
    @APIResponseSchema(Transaction.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK."),
            @APIResponse(responseCode = "201", description = "Retorno OK com a transação criada."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação da API."),
            @APIResponse(responseCode = "403", description = "Erro de autorização da API."),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado."),

    })
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/transacoes")
    @Parameter(name = "dataInicio", in = ParameterIn.QUERY, description = "Data de Início no formato yyyy-MM-dd")
    @Parameter(name = "dataFim", in = ParameterIn.QUERY, description = "Data Final no formato yyyy-MM-dd")
    public Response buscarTransacoes(@QueryParam(value = "dataInicio") final String dataInicio,
            @QueryParam(value = "dataFim") final String dataFim) throws ParseException {

        return Response.ok(pixService.buscarTransacoes(DATE_FORMAT.parse(dataInicio), DATE_FORMAT.parse(dataFim)))
                .build();
    }

    @Operation(description = "API para buscar um QRCode a partir de um UUID específico.")
    @APIResponseSchema(Response.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK."),
            @APIResponse(responseCode = "201", description = "Retorno OK com a transação criada."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação da API."),
            @APIResponse(responseCode = "403", description = "Erro de autorização da API."),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado."),

    })
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("image/png")
    @Path("/{uuid}/qrcode")
    public Response qrCode(@PathParam("uuid") final String uuid) throws IOException {
        return Response.ok(pixService.gerarQrCode(uuid)).build();
    }

}
