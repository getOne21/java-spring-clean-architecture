package com.getone21.cleanarchitecture.application.service;

import com.getone21.cleanarchitecture.domain.exception.UserAlreadyExistsException;
import com.getone21.cleanarchitecture.domain.exception.UserNotFoundException;
import com.getone21.cleanarchitecture.domain.model.User;
import com.getone21.cleanarchitecture.domain.port.in.CreateUserUseCase;
import com.getone21.cleanarchitecture.domain.port.in.DeleteUserUseCase;
import com.getone21.cleanarchitecture.domain.port.in.GetUserUseCase;
import com.getone21.cleanarchitecture.domain.port.in.UpdateUserUseCase;
import com.getone21.cleanarchitecture.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService implements CreateUserUseCase, GetUserUseCase, UpdateUserUseCase, DeleteUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public UserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User createUser(User user) {
        if (userRepositoryPort.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail());
        }

        user.setId(UUID.randomUUID());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepositoryPort.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepositoryPort.findAll();
    }

    @Override
    public User updateUser(UUID id, User user) {
        User existingUser = userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!existingUser.getEmail().equals(user.getEmail())
                && userRepositoryPort.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail());
        }

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setUpdatedAt(LocalDateTime.now());

        return userRepositoryPort.save(existingUser);
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepositoryPort.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepositoryPort.deleteById(id);
    }
}
