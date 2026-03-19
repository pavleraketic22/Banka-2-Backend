package rs.raf.banka2_bek.account.controller;

import rs.raf.banka2_bek.account.dto.AccountLimitsUpdateDto;
import rs.raf.banka2_bek.account.dto.AccountNameUpdateDto;
import rs.raf.banka2_bek.account.dto.AccountResponseDto;
import rs.raf.banka2_bek.account.dto.CreateAccountDto;
import rs.raf.banka2_bek.account.service.AccountService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account", description = "Client account viewing API")
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Create account", description = "Employee creates a new checking or foreign currency account for a client or company")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody CreateAccountDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request));
    }

    @Operation(summary = "Get all accounts (employee portal)", description = "Returns paginated list of all accounts with optional owner filter")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paginated accounts")
    })
    @GetMapping("/all")
    public ResponseEntity<Page<AccountResponseDto>> getAllAccounts(
            @Parameter(description = "Page index") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Filter by owner name") @RequestParam(required = false) String ownerName) {
        return ResponseEntity.ok(accountService.getAllAccounts(page, limit, ownerName));
    }

    @Operation(summary = "Get accounts by client ID (employee portal)")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AccountResponseDto>> getAccountsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(accountService.getAccountsByClient(clientId));
    }

    @Operation(summary = "Get my accounts", description = "Returns a list of active accounts for the currently authenticated client, "
                    + "sorted by available balance in descending order."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of active accounts"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/my")
    public ResponseEntity<List<AccountResponseDto>> getMyAccounts() {
        return ResponseEntity.ok(accountService.getMyAccounts());
    }

    @Operation(summary = "Get account by ID", description = "Returns detailed information about a single account. Only account owner can access it.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account details", content = @Content(schema = @Schema(implementation = AccountResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "403", description = "User is not the owner of this account"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getAccountById(
            @Parameter(description = "Account ID") @PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    // name change
    @Operation(
            summary = "Update account name",
            description = "Changes the display name of an account. "
                    + "New name must differ from the current one and be unique among the client's accounts."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account name updated",
                    content = @Content(schema = @Schema(implementation = AccountResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "403", description = "User is not the owner or name is invalid"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PatchMapping("/{id}/name")
    public ResponseEntity<AccountResponseDto> updateAccountName(
            @Parameter(description = "Account ID") @PathVariable Long id,
            @Valid @RequestBody AccountNameUpdateDto request) {
        return ResponseEntity.ok(accountService.updateAccountName(id, request.getName()));
    }

    // limit change
    @Operation(
            summary = "Update account limits",
            description = "Changes the daily and/or monthly spending limits for an account. "
                    + "Only the account owner can change limits. "
                    + "Requires transaction verification (mobile app) — currently not implemented."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Limits updated",
                    content = @Content(schema = @Schema(implementation = AccountResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "403", description = "User is not the owner of this account"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PatchMapping("/{id}/limits")
    public ResponseEntity<AccountResponseDto> updateAccountLimits(
            @Parameter(description = "Account ID") @PathVariable Long id,
            @Valid @RequestBody AccountLimitsUpdateDto request) {
        return ResponseEntity.ok(accountService.updateAccountLimits(
                id, request.getDailyLimit(), request.getMonthlyLimit()));
    }
}
