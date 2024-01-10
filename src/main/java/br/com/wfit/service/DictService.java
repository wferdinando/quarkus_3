package br.com.wfit.service;

import java.time.LocalDateTime;
import java.util.Objects;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.wfit.config.RedisCache;
import br.com.wfit.model.Chave;
import br.com.wfit.model.TipoChave;
import br.com.wfit.model.TipoPessoa;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DictService {

    @ConfigProperty(name = "pix.chave")
    private String chave;

    @ConfigProperty(name = "pix.ispb")
    private String ispb;

    @ConfigProperty(name = "pix.cpf")
    private String cpf;

    @ConfigProperty(name = "pix.nome")
    private String nome;

    @Inject
    RedisCache redisCache;

    public Chave buscaDetalhesChave(String key) {
        var chave = buscarChaveCache(key);

        if (Objects.isNull(chave)) {
            var chaveFake = buscarChave(key);
            redisCache.set(key, buscarChave(key));
            return chaveFake;
        }

        return chave;
    }

    public Chave buscarChave(String chave) {
        return new Chave(
                TipoChave.CELULAR,
                chave,
                ispb,
                TipoPessoa.FISICA,
                cpf,
                nome,
                LocalDateTime.now());
    }

    private Chave buscarChaveCache(String key) {
        var chave = redisCache.get(key);
        Log.infof("Chave encontrada no cache %s", chave);
        return chave;
    }
}
