package rs.raf.banka2_bek.employee.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.raf.banka2_bek.employee.dto.*;
import rs.raf.banka2_bek.employee.event.EmployeeAccountCreatedEvent;
import rs.raf.banka2_bek.employee.model.Employee;
import rs.raf.banka2_bek.employee.repository.EmployeeRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    // ===================== TASK 7 =====================
    public EmployeeResponseDto createEmployee(CreateEmployeeRequestDto request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Zaposleni sa ovim emailom već postoji.");
        }
        if (employeeRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Zaposleni sa ovim usernamom već postoji.");
        }

        String activationToken = UUID.randomUUID().toString();
        Boolean active = request.getActive() != null ? request.getActive() : true;

        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .username(request.getUsername())
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .position(request.getPosition())
                .department(request.getDepartment())
                .active(active)
                .activationToken(activationToken)
                .activationTokenExpiry(LocalDateTime.now().plusDays(3))
                .build();

        employeeRepository.save(employee);

        eventPublisher.publishEvent(
                new EmployeeAccountCreatedEvent(this, employee.getEmail(), employee.getFirstName(), activationToken)
        );

        return toResponse(employee);
    }

    // ===================== TASK 8 =====================
    public void activateAccount(ActivateAccountRequestDto request) {
        Employee employee = employeeRepository.findByActivationToken(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Nevažeći aktivacioni token."));

        if (employee.getActivationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Aktivacioni token je istekao.");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Lozinke se ne poklapaju.");
        }

        validatePassword(request.getPassword());

        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setActivationToken(null);
        employee.setActivationTokenExpiry(null);

        employeeRepository.save(employee);
    }

    // ===================== TASK 9 =====================
    public Page<EmployeeResponseDto> getEmployees(int page, int limit, String email, String firstName, String lastName, String position) {
        Pageable pageable = PageRequest.of(page, limit);
        return employeeRepository.findByFilters(email, firstName, lastName, position, pageable)
                .map(this::toResponse);
    }

    // ===================== TASK 10 =====================
    public EmployeeResponseDto updateEmployee(Long id, UpdateEmployeeRequestDto request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zaposleni sa ID " + id + " ne postoji."));

        if (request.getFirstName() != null) employee.setFirstName(request.getFirstName());
        if (request.getLastName() != null) employee.setLastName(request.getLastName());
        if (request.getDateOfBirth() != null) employee.setDateOfBirth(request.getDateOfBirth());
        if (request.getGender() != null) employee.setGender(request.getGender());
        if (request.getPhone() != null) employee.setPhone(request.getPhone());
        if (request.getAddress() != null) employee.setAddress(request.getAddress());
        if (request.getPosition() != null) employee.setPosition(request.getPosition());
        if (request.getDepartment() != null) employee.setDepartment(request.getDepartment());

        employeeRepository.save(employee);
        return toResponse(employee);
    }

    // ===================== TASK 11 =====================
    public void deactivateEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zaposleni sa ID " + id + " ne postoji."));

        if (!employee.getActive()) {
            throw new IllegalStateException("Nalog je već deaktiviran.");
        }

        employee.setActive(false);
        employeeRepository.save(employee);
    }

    // ===================== HELPERS =====================
    private void validatePassword(String password) {
        if (password.length() < 8 || password.length() > 32) {
            throw new IllegalArgumentException("Lozinka mora imati između 8 i 32 karaktera.");
        }
        if (password.chars().filter(Character::isDigit).count() < 2) {
            throw new IllegalArgumentException("Lozinka mora sadržati najmanje 2 broja.");
        }
        if (password.chars().noneMatch(Character::isUpperCase)) {
            throw new IllegalArgumentException("Lozinka mora sadržati najmanje 1 veliko slovo.");
        }
        if (password.chars().noneMatch(Character::isLowerCase)) {
            throw new IllegalArgumentException("Lozinka mora sadržati najmanje 1 malo slovo.");
        }
    }

    private EmployeeResponseDto toResponse(Employee employee) {
        return EmployeeResponseDto.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .dateOfBirth(employee.getDateOfBirth())
                .gender(employee.getGender())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .address(employee.getAddress())
                .username(employee.getUsername())
                .position(employee.getPosition())
                .department(employee.getDepartment())
                .active(employee.getActive())
                .build();
    }
}