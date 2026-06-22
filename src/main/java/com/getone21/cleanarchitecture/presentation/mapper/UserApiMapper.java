package com.getone21.cleanarchitecture.presentation.mapper;

import com.getone21.cleanarchitecture.domain.model.User;
import com.getone21.cleanarchitecture.presentation.dto.request.CreateUserRequest;
import com.getone21.cleanarchitecture.presentation.dto.request.UpdateUserRequest;
import com.getone21.cleanarchitecture.presentation.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserApiMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toDomain(CreateUserRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toDomain(UpdateUserRequest request);

    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);
}
