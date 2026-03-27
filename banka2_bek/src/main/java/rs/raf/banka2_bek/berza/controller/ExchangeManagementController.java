package rs.raf.banka2_bek.berza.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.raf.banka2_bek.berza.dto.ExchangeDto;
import rs.raf.banka2_bek.berza.service.ExchangeManagementService;

import java.util.List;
import java.util.Map;

/**
 * REST kontroler za upravljanje berzama.
 *
 * Endpointovi:
 *   GET  /exchanges              - lista svih aktivnih berzi sa statusom
 *   GET  /exchanges/{acronym}    - detalji jedne berze
 *   PATCH /exchanges/{acronym}/test-mode - ukljuci/iskljuci test mode (samo admin)
 *
 * Specifikacija: Celina 3 - Berza
 */
@RestController
@RequestMapping("/exchanges")
@RequiredArgsConstructor
public class ExchangeManagementController {

    private final ExchangeManagementService exchangeManagementService;

    /**
     * GET /exchanges
     * Vraca listu svih aktivnih berzi sa computed statusom (isOpen, currentLocalTime, nextOpenTime).
     *
     * TODO: Implementirati:
     *   1. Pozvati exchangeManagementService.getAllExchanges()
     *   2. Vratiti ResponseEntity.ok(lista)
     */
    @GetMapping
    public ResponseEntity<List<ExchangeDto>> getAllExchanges() {
        // TODO: Implementirati prema uputstvima iznad
        throw new UnsupportedOperationException("GET /exchanges nije implementiran");
    }

    /**
     * GET /exchanges/{acronym}
     * Vraca detalje jedne berze sa computed statusom.
     *
     * TODO: Implementirati:
     *   1. Pozvati exchangeManagementService.getByAcronym(acronym)
     *   2. Vratiti ResponseEntity.ok(dto)
     *   3. Hendlati slucaj kada berza ne postoji (404)
     */
    @GetMapping("/{acronym}")
    public ResponseEntity<ExchangeDto> getByAcronym(@PathVariable String acronym) {
        // TODO: Implementirati prema uputstvima iznad
        throw new UnsupportedOperationException("GET /exchanges/{acronym} nije implementiran");
    }

    /**
     * PATCH /exchanges/{acronym}/test-mode
     * Ukljucuje/iskljucuje test mode za berzu. Samo admin moze pristupiti.
     *
     * Request body: { "enabled": true/false }
     *
     * TODO: Implementirati:
     *   1. Procitati "enabled" iz request body-ja
     *   2. Pozvati exchangeManagementService.setTestMode(acronym, enabled)
     *   3. Vratiti ResponseEntity.ok() sa porukom potvrde
     */
    @PatchMapping("/{acronym}/test-mode")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> setTestMode(
            @PathVariable String acronym,
            @RequestBody Map<String, Boolean> body) {
        // TODO: Implementirati prema uputstvima iznad
        throw new UnsupportedOperationException("PATCH /exchanges/{acronym}/test-mode nije implementiran");
    }
}
