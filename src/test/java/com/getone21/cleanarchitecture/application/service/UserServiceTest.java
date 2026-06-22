package com.getone21.cleanarchitecture.application.service;

import com.getone21.cleanarchitecture.domain.exception.UserAlreadyExistsException;
import com.getone21.cleanarchitecture.domain.exception.UserNotFoundException;
import com.getone21.cleanarchitecture.domain.model.User;
import com.getone21.cleanarchitecture.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@example.com");
    }

    @Nested
    @DisplayName("Create User")
    class CreateUser {

        @Test
        @DisplayName("should create user successfully")
        void shouldCreateUser() {
            when(userRepositoryPort.existsByEmail(testUser.getEmail())).thenReturn(false);
            when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            User result = userService.createUser(testUser);

            assertThat(result.getId()).isNotNull();
            assertThat(result.getFirstName()).isEqualTo("John");
            assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
            assertThat(result.getCreatedAt()).isNotNull();
            verify(userRepositoryPort).save(any(User.class));
        }

        @Test
        @DisplayName("should throw exception when email already exists")
        void shouldThrowWhenEmailExists() {
            when(userRepositoryPort.existsByEmail(testUser.getEmail())).thenReturn(true);

            assertThatThrownBy(() -> userService.createUser(testUser))
                    .isInstanceOf(UserAlreadyExistsException.class)
                    .hasMessageContaining("john.doe@example.com");

            verify(userRepositoryPort, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Get User")
    class GetUser {

        @Test
        @DisplayName("should return user by id")
        void shouldReturnUserById() {
            UUID id = UUID.randomUUID();
            testUser.setId(id);
            when(userRepositoryPort.findById(id)).thenReturn(Optional.of(testUser));

            User result = userService.getUserById(id);

            assertThat(result.getId()).isEqualTo(id);
            assertThat(result.getFirstName()).isEqualTo("John");
        }

        @Test
        @DisplayName("should throw exception when user not found")
        void shouldThrowWhenUserNotFound() {
            UUID id = UUID.randomUUID();
            when(userRepositoryPort.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getUserById(id))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        @DisplayName("should return all users")
        void shouldReturnAllUsers() {
            when(userRepositoryPort.findAll()).thenReturn(List.of(testUser));

            List<User> result = userService.getAllUsers();

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Update User")
    class UpdateUser {

        @Test
        @DisplayName("should update user successfully")
        void shouldUpdateUser() {
            UUID id = UUID.randomUUID();
            testUser.setId(id);
            testUser.setEmail("john.doe@example.com");

            User updateData = new User();
            updateData.setFirstName("Jane");
            updateData.setLastName("Doe");
            updateData.setEmail("john.doe@example.com");

            when(userRepositoryPort.findById(id)).thenReturn(Optional.of(testUser));
            when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            User result = userService.updateUser(id, updateData);

            assertThat(result.getFirstName()).isEqualTo("Jane");
            assertThat(result.getUpdatedAt()).isNotNull();
        }

        @Test
        @DisplayName("should throw exception when changing to existing email")
        void shouldThrowWhenChangingToExistingEmail() {
            UUID id = UUID.randomUUID();
            testUser.setId(id);
            testUser.setEmail("john.doe@example.com");

            User updateData = new User();
            updateData.setFirstName("John");
            updateData.setLastName("Doe");
            updateData.setEmail("existing@example.com");

            when(userRepositoryPort.findById(id)).thenReturn(Optional.of(testUser));
            when(userRepositoryPort.existsByEmail("existing@example.com")).thenReturn(true);

            assertThatThrownBy(() -> userService.updateUser(id, updateData))
                    .isInstanceOf(UserAlreadyExistsException.class);
        }
    }

    @Nested
    @DisplayName("Delete User")
    class DeleteUser {

        @Test
        @DisplayName("should delete user successfully")
        void shouldDeleteUser() {
            UUID id = UUID.randomUUID();
            when(userRepositoryPort.existsById(id)).thenReturn(true);

            userService.deleteUser(id);

            verify(userRepositoryPort).deleteById(id);
        }

        @Test
        @DisplayName("should throw exception when user not found")
        void shouldThrowWhenDeletingNonExistent() {
            UUID id = UUID.randomUUID();
            when(userRepositoryPort.existsById(id)).thenReturn(false);

            assertThatThrownBy(() -> userService.deleteUser(id))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }
}
