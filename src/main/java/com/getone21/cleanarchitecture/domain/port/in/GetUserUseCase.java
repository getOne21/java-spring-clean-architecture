package com.getone21.cleanarchitecture.domain.port.in;

import com.getone21.cleanarchitecture.domain.model.User;

import java.util.List;
import java.util.UUID;

public interface GetUserUseCase {

    User getUserById(UUID id);

    List<User> getAllUsers();
}
