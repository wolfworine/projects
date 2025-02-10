package pe.com.interbank.infrastructure.adapter.output.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("transfer")
public class BalanceEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    private String phoneNumber;
    private Long originAccount;
    private BigDecimal balance;
    private LocalDateTime lastUpdate;
}
