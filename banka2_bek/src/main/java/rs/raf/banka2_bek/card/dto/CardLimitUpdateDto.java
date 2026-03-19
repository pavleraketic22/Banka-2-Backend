package rs.raf.banka2_bek.card.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardLimitUpdateDto {
    @NotNull(message = "Limit je obavezan")
    @PositiveOrZero(message = "Limit mora biti 0 ili veci")
    private BigDecimal cardLimit;
}
