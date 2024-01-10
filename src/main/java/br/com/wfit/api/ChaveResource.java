package br.com.wfit.api;

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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{chave}")
    public Response buscar(@PathParam("chave") final String chave) {
        Chave chaveCached = dictService.buscaDetalhesChave(chave);
        return Response.ok(chaveCached).build();
    }
}
