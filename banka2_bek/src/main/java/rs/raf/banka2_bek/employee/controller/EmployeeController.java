package rs.raf.banka2_bek.employee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.banka2_bek.employee.dto.*;
import rs.raf.banka2_bek.employee.service.EmployeeService;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // TASK 7 - POST /employees
    @PostMapping("/employees")
    public ResponseEntity<EmployeeResponseDto> createEmployee(@RequestBody CreateEmployeeRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(request));
    }

    // TASK 8 - POST /auth/activate
    // Zaposleni šalje token + novu lozinku
    @PostMapping("/auth/activate")
    public ResponseEntity<Void> activateAccount(@RequestBody ActivateAccountRequestDto request) {
        employeeService.activateAccount(request);
        return ResponseEntity.ok().build();
    }

    // TASK 9 - GET /employees?page=0&limit=10&email=&firstName=&lastName=&position=
    @GetMapping("/employees")
    public ResponseEntity<Page<EmployeeResponseDto>> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String position) {
        return ResponseEntity.ok(employeeService.getEmployees(page, limit, email, firstName, lastName, position));
    }

    // TASK 10 - PUT /employees/{id}
    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @PathVariable Long id,
            @RequestBody UpdateEmployeeRequestDto request) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    // TASK 11 - PATCH /employees/{id}/deactivate
    @PatchMapping("/employees/{id}/deactivate")
    public ResponseEntity<Void> deactivateEmployee(@PathVariable Long id) {
        employeeService.deactivateEmployee(id);
        return ResponseEntity.ok().build();
    }
}