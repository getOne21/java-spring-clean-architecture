package com.getone21.cleanarchitecture.domain.port.in;

import com.getone21.cleanarchitecture.domain.model.User;

import java.util.UUID;

public interface UpdateUserUseCase {

    User updateUser(UUID id, User user);
}
