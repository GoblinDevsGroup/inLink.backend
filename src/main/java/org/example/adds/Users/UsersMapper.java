package org.example.adds.Users;

import org.springframework.stereotype.Component;

@Component
public class UsersMapper {
    public UsersDto toDto(Users user) {
        if (user == null) {
            return null;
        }
        UsersDto usersDto = new UsersDto();
        usersDto.setId(user.getId());
        usersDto.setFullName(user.getFullName());
        usersDto.setPhone(user.getPhone());
        usersDto.setCompanyName(user.getCompanyName());
        usersDto.setCreatedAt(user.getCreatedAt());
        usersDto.setUpdatedAt(user.getUpdatedAt());
        return usersDto;

    }
}
