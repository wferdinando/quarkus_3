package br.com.wfit.model;

public enum StatusPix {
    CREATED, // Transação criada, não finalizada.

    IN_PROCESS, // Transação em processo, não finalizada.

    APPROVED, // Transação aprovada, não finalizada.

    REPROVED, // Transação reprovada, não finalizada.

    DONE, // Transação concluída com sucesso.

    UNDONE, // Transação não pode ser conlcuída. O valor foi estornado.

    CANCELED // Transação foi cancelada. O saldo não foi afetado.
}
