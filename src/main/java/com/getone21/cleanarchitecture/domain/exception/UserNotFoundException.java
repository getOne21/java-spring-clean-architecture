package com.getone21.cleanarchitecture.domain.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UUID id) {
        super("User not found with id: " + id);
    }

    public UserNotFoundException(String mail) {
        super("User not found with mail: " + mail);
    }
}
