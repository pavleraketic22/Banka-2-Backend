package rs.raf.banka2_bek.employee.dto;

import lombok.Data;

import java.time.LocalDate;

// DTO za kreiranje zaposlenog (Task 7)
@Data
public class CreateEmployeeRequestDto {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String email;
    private String phone;
    private String address;
    private String username;
    private String position;
    private String department;
    private Boolean active;
}