package br.com.wfit.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.wfit.config.LinhaDigitavelCache;
import br.com.wfit.domain.TransacationDomain;
import br.com.wfit.model.Chave;
import br.com.wfit.model.LinhaDigitavel;
import br.com.wfit.model.Transaction;
import br.com.wfit.model.qrcode.DadosEnvio;
import br.com.wfit.model.qrcode.QrCode;
import br.com.wfit.repository.S3ImageClientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@ApplicationScoped
public class PixService {

    @Inject
    TransacationDomain transactionDomain;

    @Inject
    LinhaDigitavelCache linhaDigitavelCache;

    @Inject
    S3ImageClientRepository s3ImageClientRepository;

    public static final String QRCODE_PATH = "/tmp/imgQrCode";

    public BufferedInputStream gerarQrCode(final String uuid) throws IOException {
        return new BufferedInputStream(s3ImageClientRepository.getObjects(uuid)
                .asInputStream());
    }

    public LinhaDigitavel gerarLinhaDigitavel(final Chave chave, BigDecimal valor, String cidadeRemetente) {

        var qrCode = new QrCode(new DadosEnvio(chave, valor, cidadeRemetente));
        var uuid = UUID.randomUUID().toString();
        var imagePath = QRCODE_PATH + uuid + ".png";
        qrCode.save(Path.of(imagePath));
        this.salvarImagemS3(uuid, imagePath);
        String qrCodeString = qrCode.toString();
        var linhaDigitavel = new LinhaDigitavel(qrCodeString, uuid);
        salvarLinhaDigitavel(chave, valor, linhaDigitavel);
        return linhaDigitavel;

    }

    private PutObjectResponse salvarImagemS3(String uuid, String imagePath) {
        return s3ImageClientRepository.putObject(Paths.get(imagePath), uuid);
    }

    private void salvarLinhaDigitavel(Chave chave, BigDecimal valor, LinhaDigitavel linhaDigitavel) {
        transactionDomain.adicionarTransacao(linhaDigitavel, valor, chave);
        linhaDigitavelCache.set(linhaDigitavel.uuid(), linhaDigitavel);
    }

    public Optional<Transaction> findById(final String uuid) {
        return transactionDomain.findById(uuid);
    }

    public Optional<Transaction> aprovarTransacao(final String uuid) {
        return transactionDomain.aprovarTransacao(uuid);
    }

    private void processarPix() {

    }

    public List<Transaction> buscarTransacoes(final Date dataInicio, final Date dataFim) {
        return transactionDomain.buscarTransacoes(dataInicio, dataFim);
    }

    public Optional<Transaction> reprovarTransacao(final String uuid) {
        return transactionDomain.reprovarTransacao(uuid);
    }

}
