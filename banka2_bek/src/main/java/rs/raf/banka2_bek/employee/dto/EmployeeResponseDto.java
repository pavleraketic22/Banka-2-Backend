package rs.raf.banka2_bek.employee.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

// Response DTO - bez passworda, tokena i sl. (Task 9)
@Data
@Builder
public class EmployeeResponseDto {
    private Long id;
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