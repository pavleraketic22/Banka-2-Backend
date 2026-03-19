package rs.raf.banka2_bek.account.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import rs.raf.banka2_bek.account.model.AccountSubtype;
import rs.raf.banka2_bek.account.model.AccountType;

import java.math.BigDecimal;

@Data
public class CreateAccountDto {

    @NotNull(message = "Tip racuna je obavezan")
    private AccountType accountType;

    private AccountSubtype accountSubtype;

    @NotNull(message = "Valuta je obavezna")
    private String currencyCode;

    @PositiveOrZero(message = "Pocetno stanje mora biti 0 ili vece")
    private BigDecimal initialBalance = BigDecimal.ZERO;

    private Long clientId;

    private CreateAccountCompanyDto company;

    private Boolean createCard = false;

    private BigDecimal dailyLimit;
    private BigDecimal monthlyLimit;
}
