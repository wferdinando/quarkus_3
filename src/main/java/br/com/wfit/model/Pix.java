package br.com.wfit.model;

import java.math.BigDecimal;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record Pix(
        @Schema(description = "Chave cadastrada do recebedor") String chave,

        @Schema(description = "Valor da transação") BigDecimal valor,

        @Schema(description = "Cidade remetente do recebedor") String cidadeRemetente) {

}
