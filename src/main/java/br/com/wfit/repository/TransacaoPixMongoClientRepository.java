package br.com.wfit.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;

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
        var document = new Document();
        document.append(TransactionConverterApply.ID, linhaDigitavel.uuid())
                .append(TransactionConverterApply.VALOR, valor)
                .append(TransactionConverterApply.TIPO_CHAVE, chave.tipoChave())
                .append(TransactionConverterApply.CHAVE, chave.chave())
                .append(TransactionConverterApply.LINHA, linhaDigitavel.linha())
                .append(TransactionConverterApply.STATUS, StatusPix.CREATED)
                .append(TransactionConverterApply.DATA, LocalDateTime.now(ZoneId.of(AMERICA_SAO_PAULO)));
                getCollection().insertOne(document);
    }


    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("pix").getCollection("transacao_pix");
    }

    @Override
    public Optional<Transaction> alterarStatusTransacao(String uuid, StatusPix statusPix) {

        Optional<Document> optionalDocument = findOne(uuid);

        if (optionalDocument.isPresent()) {

            Document document = optionalDocument.get();
            var opts = new FindOneAndReplaceOptions().upsert(false).returnDocument(ReturnDocument.AFTER);
            document.merge(TransactionConverterApply.STATUS, statusPix, (a, b) -> b);

            var replace = getCollection().findOneAndReplace(Filters.eq(TransactionConverterApply.ID, uuid), document,
                    opts);

            assert replace != null;

            return Optional.ofNullable(TransactionConverterApply.apply(replace));
        }

        return Optional.empty();
    }

    @Override
    public Optional<Document> findOne(String uuid) {

        var filter = Filters.eq(TransactionConverterApply.ID, uuid);
        FindIterable<Document> documents = getCollection().find(filter);
        return StreamSupport.stream(documents.spliterator(), false).findFirst();

    }

}
