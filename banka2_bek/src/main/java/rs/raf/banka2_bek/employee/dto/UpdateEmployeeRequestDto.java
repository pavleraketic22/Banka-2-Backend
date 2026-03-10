package rs.raf.banka2_bek.employee.dto;

import lombok.Data;

import java.time.LocalDate;

// DTO za update zaposlenog (Task 10)
@Data
public class UpdateEmployeeRequestDto {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String phone;
    private String address;
    private String position;
    private String department;
    // email i username se ne menjaju
}