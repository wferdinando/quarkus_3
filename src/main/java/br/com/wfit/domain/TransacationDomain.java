package br.com.wfit.domain;

import java.math.BigDecimal;

import br.com.wfit.model.Chave;
import br.com.wfit.model.LinhaDigitavel;
import br.com.wfit.repository.TransacaoPixMongoClientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TransacationDomain {

    @Inject
    TransacaoPixMongoClientRepository repository;

    @Transactional
    public void adicionarTransacao(final LinhaDigitavel linhaDigitavel, final BigDecimal valor, final Chave chave) {
        repository.adicionar(linhaDigitavel, valor, chave);
    }

}
