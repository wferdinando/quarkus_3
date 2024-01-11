package br.com.wfit.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

import br.com.wfit.domain.TransactionConverterApply;
import br.com.wfit.model.Chave;
import br.com.wfit.model.LinhaDigitavel;
import br.com.wfit.model.StatusPix;
import br.com.wfit.model.Transaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransacaoPixMongoClientRepository implements TransactionRepository {

    public static final String AMERICA_SAO_PAULO = "America/Sao_Paulo";

    @Inject
    MongoClient mongoClient;

    @Override
    public void adicionar(LinhaDigitavel linhaDigitavel, BigDecimal valor, Chave chave) {

        Document document = new Document();

        document.append(TransactionConverterApply.ID, linhaDigitavel.uuid())
                .append(TransactionConverterApply.VALOR, valor)
                .append(TransactionConverterApply.TIPO_CHAVE, chave.tipoChave())
                .append(TransactionConverterApply.CHAVE, chave.chave())
                .append(TransactionConverterApply.LINHA, linhaDigitavel.linha())
                .append(TransactionConverterApply.DATA, LocalDateTime.now(ZoneId.of(AMERICA_SAO_PAULO)));

        getCollection().insertOne(document);
    }

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("pix").getCollection("transacao_pix");
    }

    @Override
    public Optional<Transaction> alterarStatusTransacao(String uuid, StatusPix statusPix) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'alterarStatusTransacao'");
    }

    @Override
    public Optional<Document> findOne(String uuid) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

}
