package br.com.wfit.service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import br.com.wfit.model.Chave;
import br.com.wfit.model.LinhaDigitavel;
import br.com.wfit.model.qrcode.DadosEnvio;
import br.com.wfit.model.qrcode.QrCode;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PixService {

    public static final String QRCODE_PATH = "/tmp/imgQrCode";

    public BufferedInputStream gerarQrCode(final String uuid) throws IOException {

        // TODO RECUPERAR DA CACHE
        var imagePath = QRCODE_PATH + uuid + ".png";

        try {
            return new BufferedInputStream(new FileInputStream(imagePath));
        } finally {
            Files.delete(Paths.get(imagePath));
        }
    }

    public LinhaDigitavel gerarLinhaDigitavel(final Chave chave, BigDecimal valor, String cidadeRemetente) {

        QrCode qrCode = new QrCode(new DadosEnvio(chave, valor, cidadeRemetente));
        String uuid = UUID.randomUUID().toString();

        var imagePath = QRCODE_PATH + uuid + ".png";
        qrCode.save(Path.of(imagePath));

        // TODO IMPLEMENTAR CACHE

        String qrCodeString = qrCode.toString();

        return new LinhaDigitavel(qrCodeString, uuid);
    }

}
