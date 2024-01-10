package br.com.wfit.service;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.UUID;

import br.com.wfit.model.Chave;
import br.com.wfit.model.LinhaDigitavel;
import br.com.wfit.model.qrcode.DadosEnvio;
import br.com.wfit.model.qrcode.QrCode;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PixService {

    public static final String QRCODE_PATH = "/tmp/imgQrCode";

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
