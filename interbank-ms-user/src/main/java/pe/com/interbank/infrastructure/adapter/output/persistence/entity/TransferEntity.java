package pe.com.interbank.infrastructure.adapter.output.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import pe.com.interbank.infrastructure.adapter.input.rest.model.enums.TransferStatusEnum;
import pe.com.interbank.infrastructure.adapter.input.rest.model.enums.TransferTypeEnum;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("transfer")
public class TransferEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String originNumber;
    private Long originAccount;
    private String targetNumber;
    private Long targetAccount;
    private BigDecimal amount;
    private TransferTypeEnum transferType;
    private TransferStatusEnum transferStatus;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
