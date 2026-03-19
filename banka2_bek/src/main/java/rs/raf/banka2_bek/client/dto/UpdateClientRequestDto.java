package rs.raf.banka2_bek.client.dto;

import lombok.Data;

@Data
public class UpdateClientRequestDto {
    private String lastName;
    private String gender;
    private String phone;
    private String address;
}
