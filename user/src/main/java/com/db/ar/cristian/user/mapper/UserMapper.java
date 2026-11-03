package com.db.ar.cristian.user.mapper;

import com.db.ar.cristian.user.dto.UserRequest;
import com.db.ar.cristian.user.dto.UserResponse;
import com.db.ar.cristian.user.model.User;
import com.db.ar.cristian.user.model.UserRating;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface UserMapper {


    User toEntity(UserRequest request);

    @AfterMapping
    default void afterMapping(@MappingTarget User user){
        user.setUserRating(UserRating.GOOD);
        user.setAccountLimit(new BigDecimal("10000.00"));
    }

    UserResponse toResponse(User user);
}
