package rs.raf.banka2_bek.card.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.banka2_bek.card.dto.CardLimitUpdateDto;
import rs.raf.banka2_bek.card.dto.CardResponseDto;
import rs.raf.banka2_bek.card.dto.CreateCardRequestDto;
import rs.raf.banka2_bek.card.service.CardService;

import java.util.List;

@Tag(name = "Cards", description = "Card management API")
@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @Operation(summary = "Create card", description = "Client requests a new card for their account")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Card created"),
            @ApiResponse(responseCode = "400", description = "Max cards reached or invalid account")
    })
    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(@Valid @RequestBody CreateCardRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(request));
    }

    @Operation(summary = "Get my cards", description = "Returns all cards for authenticated client")
    @GetMapping
    public ResponseEntity<List<CardResponseDto>> getMyCards() {
        return ResponseEntity.ok(cardService.getMyCards());
    }

    @Operation(summary = "Get cards by account", description = "Returns all cards for a specific account (employee portal)")
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<CardResponseDto>> getCardsByAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(cardService.getCardsByAccount(accountId));
    }

    @Operation(summary = "Block card", description = "Client blocks their own card")
    @PatchMapping("/{id}/block")
    public ResponseEntity<CardResponseDto> blockCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.blockCard(id));
    }

    @Operation(summary = "Unblock card", description = "Employee unblocks a blocked card")
    @PatchMapping("/{id}/unblock")
    public ResponseEntity<CardResponseDto> unblockCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.unblockCard(id));
    }

    @Operation(summary = "Deactivate card", description = "Employee permanently deactivates a card")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CardResponseDto> deactivateCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.deactivateCard(id));
    }

    @Operation(summary = "Update card limit")
    @PatchMapping("/{id}/limit")
    public ResponseEntity<CardResponseDto> updateCardLimit(
            @PathVariable Long id,
            @Valid @RequestBody CardLimitUpdateDto request) {
        return ResponseEntity.ok(cardService.updateCardLimit(id, request.getCardLimit()));
    }
}
