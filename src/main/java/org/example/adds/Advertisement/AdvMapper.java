package org.example.adds.Advertisement;


import org.example.adds.Users.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring",imports = java.time.LocalDateTime.class)
public interface AdvMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "INACTIVE")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "visitorNumber", constant = "0")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "advLink", source = "advLink")
    Advertisement toEntity(AdvLink request, Users user, String advLink);

    AdvResponse toResponse(Advertisement advertisement);
    List<AdvResponse> advertisementListToAdvResponseList(List<Advertisement> advertisements);

}
