package rs.raf.banka2_bek.transfers.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import rs.raf.banka2_bek.client.model.Client;
import rs.raf.banka2_bek.client.repository.ClientRepository;
import rs.raf.banka2_bek.transfers.dto.*;
import rs.raf.banka2_bek.transfers.service.TransferService;

import java.util.List;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;
    private final ClientRepository clientRepository;

    @PostMapping("/internal")
    public ResponseEntity<TransferResponseDto> internalTransfer(@RequestBody TransferInternalRequestDto request) {
        return ResponseEntity.ok(transferService.internalTransfer(request));
    }

    @PostMapping("/fx")
    public ResponseEntity<TransferResponseDto> fxTransfer(@RequestBody TransferFxRequestDto request) {
        return ResponseEntity.ok(transferService.fxTransfer(request));
    }

    @GetMapping
    public ResponseEntity<List<TransferResponseDto>> getAllTransfers() {
        Client client = getAuthenticatedClient();
        return ResponseEntity.ok(transferService.getAllTransfers(client));
    }

    @GetMapping("/{transferId}")
    public ResponseEntity<TransferResponseDto> getTransferById(@PathVariable Long transferId) {
        return ResponseEntity.ok(transferService.getTransferById(transferId));
    }

    private Client getAuthenticatedClient() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email;
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else {
            email = principal.toString();
        }
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Klijent nije pronadjen"));
    }
}
