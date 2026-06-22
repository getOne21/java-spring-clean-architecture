package com.getone21.cleanarchitecture.domain.port.in;

import java.util.UUID;

public interface DeleteUserUseCase {

    void deleteUser(UUID id);
}
