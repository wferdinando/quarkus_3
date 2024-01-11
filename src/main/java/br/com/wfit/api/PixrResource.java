package br.com.wfit.api;

import java.io.IOException;
import java.util.Objects;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import br.com.wfit.model.Chave;
import br.com.wfit.model.LinhaDigitavel;
import br.com.wfit.model.Pix;
import br.com.wfit.service.DictService;
import br.com.wfit.service.PixService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/pix")
public class PixrResource {

    @Inject
    DictService dictService;

    @Inject
    PixService pixService;
  

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
    @Path("/qrcode/{uuid}")
    public Response qrCode(@PathParam("uuid") final String uuid) throws IOException{
        return Response.ok(pixService.gerarQrCode(uuid)).build();
    }
}
