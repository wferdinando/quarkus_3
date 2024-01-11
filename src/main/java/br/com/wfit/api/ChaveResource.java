package br.com.wfit.api;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import br.com.wfit.model.Chave;
import br.com.wfit.service.DictService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/chaves")
public class ChaveResource {

    @Inject
    DictService dictService;

    @Operation(description = "API para buscar detalhes de uma Chave PIX.")
    @APIResponseSchema(Response.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK."),
            @APIResponse(responseCode = "201", description = "Retorno OK com a transação criada."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação da API."),
            @APIResponse(responseCode = "403", description = "Erro de autorização da API."),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado."),

    })

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{chave}")
    public Response buscar(@PathParam("chave") final String chave) {
        Chave chaveCached = dictService.buscaDetalhesChave(chave);
        return Response.ok(chaveCached).build();
    }
}
