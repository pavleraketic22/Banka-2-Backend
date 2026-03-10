package rs.raf.banka2_bek.employee.dto;

import lombok.Data;

// DTO za aktivaciju naloga (Task 8)
// Zaposleni unosi lozinku pri aktivaciji
@Data
public class ActivateAccountRequestDto {
    private String token;
    private String password;
    private String confirmPassword;
}