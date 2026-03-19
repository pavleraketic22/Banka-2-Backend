package rs.raf.banka2_bek.card.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateCardRequestDto {
    @NotNull(message = "Account ID je obavezan")
    private Long accountId;
    private BigDecimal cardLimit;
}
