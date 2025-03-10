package org.example.adds.Users;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UsersMapper {
    public UsersDto toDto(Users user, String cashId) {
        if (user == null) {
            return null;
        }
        UsersDto usersDto = new UsersDto();
        usersDto.setId(user.getId());
        usersDto.setFullName(user.getFullName());
        usersDto.setPhone(user.getPhone());
        usersDto.setCashId(cashId);
        usersDto.setCompanyName(user.getCompanyName());
        return usersDto;
    }
}
