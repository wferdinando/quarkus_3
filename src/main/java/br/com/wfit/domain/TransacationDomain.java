package br.com.wfit.domain;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.kafka.common.header.internals.RecordHeaders;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

import br.com.wfit.model.Chave;
import br.com.wfit.model.LinhaDigitavel;
import br.com.wfit.model.StatusPix;
import br.com.wfit.model.Transaction;
import br.com.wfit.repository.TransactionPanacheRepository;
import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransacationDomain {

    @Inject
    TransactionPanacheRepository repository;

    @Inject
    @Channel("transacao")
    Emitter<Transaction> transactionEmitter;

    private void enviarTransacao(final Transaction transaction) {
        Log.infof("Enviando mensagem de nova transação: %s", transaction);
        transactionEmitter.send(
                Message.of(transaction).addMetadata(OutgoingKafkaRecordMetadata.<String>builder()
                        .withKey(transaction.getId())
                        .withHeaders(new RecordHeaders().add("x-linha",
                                transaction.getLinha().getBytes(StandardCharsets.UTF_8)))
                        .build()));
    }

    public void adicionarTransacao(final LinhaDigitavel linhaDigitavel, final BigDecimal valor, final Chave chave) {
        repository.adicionar(linhaDigitavel, valor, chave);
    }

    public Optional<Transaction> aprovarTransacao(final String uuid) {

        Optional<Transaction> optTransaction = repository.alterarStatusTransacao(uuid, StatusPix.APPROVED);
        optTransaction.ifPresent(this::enviarTransacao);
        return optTransaction;
    }

    public List<Transaction> buscarTransacoes(final Date dataInicio, final Date dataFim) {
        return repository.buscarTransacoes(dataInicio, dataFim);
    }

    public Optional<Transaction> reprovarTransacao(final String uuid) {
        return repository.alterarStatusTransacao(uuid, StatusPix.REPROVED);
    }

    public Optional<Transaction> iniciarProcessamento(final String uuid) {
        return repository.alterarStatusTransacao(uuid, StatusPix.IN_PROCESS);
    }

    public Optional<Transaction> findById(final String uuid) {
        return repository.findOne(uuid);
    }

}
