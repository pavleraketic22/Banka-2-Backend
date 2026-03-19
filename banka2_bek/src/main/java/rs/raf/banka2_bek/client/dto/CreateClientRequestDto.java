package rs.raf.banka2_bek.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateClientRequestDto {
    @NotBlank(message = "Ime je obavezno")
    private String firstName;

    @NotBlank(message = "Prezime je obavezno")
    private String lastName;

    @NotNull(message = "Datum rodjenja je obavezan")
    private LocalDate dateOfBirth;

    private String gender;

    @NotBlank(message = "Email je obavezan")
    @Email(message = "Email format nije validan")
    private String email;

    @NotBlank(message = "Telefon je obavezan")
    private String phone;

    private String address;
}
