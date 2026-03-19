package rs.raf.banka2_bek.account.dto;

import lombok.Data;

@Data
public class CreateAccountCompanyDto {
    private String name;
    private String registrationNumber;
    private String taxNumber;
    private String activityCode;
    private String address;
}
