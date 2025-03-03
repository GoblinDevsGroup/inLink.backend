package org.example.adds.Users;

import lombok.Data;

import java.util.UUID;

@Data
public class UsersDto {
    private UUID id;
    private String fullName;
    private String companyName;
    private String cashId;
    private String phone;
    public UsersDto() {
    }

    public UsersDto(UUID id,
                    String fullName,
                    String companyName,
                    String cashId,
                    String phone) {
        this.id = id;
        this.fullName = fullName;
        this.companyName = companyName;
        this.cashId = cashId;
        this.phone = phone;
    }
}
