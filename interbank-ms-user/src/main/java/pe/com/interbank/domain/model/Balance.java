package pe.com.interbank.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Balance implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    private String phoneNumber;
    private Long originAccount;
    @JsonProperty("balance")
    private BigDecimal currentBalance;
    private LocalDateTime lastUpdate;

}
