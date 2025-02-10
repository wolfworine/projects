package pe.com.interbank.infrastructure.adapter.input.rest.model.input;

import pe.com.interbank.infrastructure.adapter.input.rest.model.enums.TransferStatusEnum;
import pe.com.interbank.infrastructure.adapter.input.rest.model.enums.TransferTypeEnum;

import java.math.BigDecimal;

public record TransferRequest(String originNumber, Long originAccount, String targetNumber,
        Long targetAccount, BigDecimal amount, TransferTypeEnum transferType,
        TransferStatusEnum transferStatus) {
}
