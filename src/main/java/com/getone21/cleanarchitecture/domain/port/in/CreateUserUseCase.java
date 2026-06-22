package com.getone21.cleanarchitecture.domain.port.in;

import com.getone21.cleanarchitecture.domain.model.User;

public interface CreateUserUseCase {

    User createUser(User user);
}
