package rs.raf.banka2_bek.client.service;

import org.springframework.data.domain.Page;
import rs.raf.banka2_bek.client.dto.ClientResponseDto;
import rs.raf.banka2_bek.client.dto.CreateClientRequestDto;
import rs.raf.banka2_bek.client.dto.UpdateClientRequestDto;

public interface ClientService {
    ClientResponseDto createClient(CreateClientRequestDto request);
    Page<ClientResponseDto> getClients(int page, int limit, String firstName, String lastName, String email);
    ClientResponseDto getClientById(Long id);
    ClientResponseDto updateClient(Long id, UpdateClientRequestDto request);
}
