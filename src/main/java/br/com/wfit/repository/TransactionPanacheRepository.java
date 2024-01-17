package br.com.wfit.repository;

import static br.com.wfit.repository.TransacaoPixMongoClientRepository.AMERICA_SAO_PAULO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.wfit.domain.TransactionConverterApply;
import br.com.wfit.model.Chave;
import br.com.wfit.model.LinhaDigitavel;
import br.com.wfit.model.StatusPix;
import br.com.wfit.model.Transaction;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransactionPanacheRepository implements PanacheMongoRepository<Transaction> {

    public void adicionar(LinhaDigitavel linhaDigitavel, BigDecimal valor, Chave chave) {
        var transaction = new Transaction();
        transaction.setChave(chave.chave());
        transaction.setData(LocalDateTime.now(ZoneId.of(AMERICA_SAO_PAULO)));
        transaction.setId(linhaDigitavel.uuid());
        transaction.setStatus(StatusPix.CREATED);
        transaction.setValor(valor);
        transaction.setLinha(linhaDigitavel.linha());
        transaction.setTipoChave(chave.tipoChave().toString());
        transaction.persist();
    }

    public Optional<Transaction> alterarStatusTransacao(String uuid, StatusPix statusPix) {
        Optional<Transaction> optionalTransaction = findOne(uuid);
        if (optionalTransaction.isPresent()) {
            var transaction = optionalTransaction.get();
            transaction.setStatus(statusPix);
            transaction.update();
            return Optional.of(transaction);
        }
        return Optional.empty();
    }

    public Optional<Transaction> findOne(String uuid) {
        return find(TransactionConverterApply.ID, uuid).stream().findFirst();
    }

    public List<Transaction> buscarTransacoes(final Date dataInicio, final Date dataFim) {
        return find("data >= ?1 and data <= ?2 and status = ?3", dataInicio, dataFim, StatusPix.APPROVED)
                .stream().collect(Collectors.toList());
    }

}
