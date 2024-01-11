package br.com.wfit.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.bson.Document;

import br.com.wfit.model.Chave;
import br.com.wfit.model.LinhaDigitavel;
import br.com.wfit.model.StatusPix;
import br.com.wfit.model.Transaction;


public interface TransactionRepository {
    
    void adicionar(final LinhaDigitavel linhaDigitavel, final BigDecimal valor, final Chave chave);

    Optional<Transaction> alterarStatusTransacao(final String uuid, final StatusPix statusPix);

    Optional<Document> findOne(final String uuid);
}
