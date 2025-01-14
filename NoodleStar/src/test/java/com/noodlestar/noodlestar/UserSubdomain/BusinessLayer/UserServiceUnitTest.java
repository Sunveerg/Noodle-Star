package com.noodlestar.noodlestar.UserSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.UserSubdomain.DataLayer.User;
import com.noodlestar.noodlestar.UserSubdomain.DataLayer.UserRepository;
import com.noodlestar.noodlestar.UserSubdomain.PresentationLayer.UserResponseModel;
import com.noodlestar.noodlestar.auth0.Auth0Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private Auth0Service auth0Service;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void whenAddUserFromAuth0_thenReturnUserResponseModel() {
        String auth0UserId = UUID.randomUUID().toString();
        UserResponseModel auth0User = UserResponseModel.builder()
                .userId(auth0UserId)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .roles(List.of("Customer"))
                .permissions(List.of("read:countries"))
                .build();

        User savedUser = User.builder()
                .userId(auth0UserId)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .roles(List.of("Customer"))
                .permissions(List.of("read:countries"))
                .build();

        when(auth0Service.getUserById(auth0UserId)).thenReturn(Mono.just(auth0User));
        when(auth0Service.assignRoleToUser(auth0UserId, "rol_yNoNEH6304xc31YW")).thenReturn(Mono.empty());
        when(userRepository.findByUserId(auth0UserId)).thenReturn(Mono.empty());
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(savedUser));

        StepVerifier.create(userService.addUserFromAuth0(auth0UserId))
                .expectNextMatches(response ->
                        response.getUserId().equals(auth0UserId) &&
                                response.getEmail().equals("test@example.com") &&
                                response.getRoles().contains("Customer")
                )
                .verifyComplete();
    }


    @Test
    public void whenSyncUserWithAuth0_thenReturnUpdatedUserResponseModel() {
        String auth0UserId = UUID.randomUUID().toString();
        User existingUser = User.builder()
                .userId(auth0UserId)
                .email("old@example.com")
                .firstName("Old")
                .lastName("Name")
                .roles(List.of("Customer"))
                .permissions(List.of("read:countries"))
                .build();

        UserResponseModel auth0User = UserResponseModel.builder()
                .userId(auth0UserId)
                .email("new@example.com")
                .firstName("New")
                .lastName("Name")
                .roles(List.of("Customer", "Admin"))
                .permissions(List.of("read:countries", "write:countries"))
                .build();

        User updatedUser = User.builder()
                .userId(auth0UserId)
                .email("new@example.com")
                .firstName("New")
                .lastName("Name")
                .roles(List.of("Customer", "Admin"))
                .permissions(List.of("read:countries", "write:countries"))
                .build();

        when(auth0Service.getUserById(auth0UserId)).thenReturn(Mono.just(auth0User));
        when(userRepository.findByUserId(auth0UserId)).thenReturn(Mono.just(existingUser));
        when(userRepository.save(existingUser)).thenReturn(Mono.just(updatedUser));

        StepVerifier.create(userService.syncUserWithAuth0(auth0UserId))
                .expectNextMatches(response ->
                        response.getEmail().equals("new@example.com") &&
                                response.getRoles().contains("Admin")
                )
                .verifyComplete();
    }


    @Test
    public void whenGetUserByUserId_thenReturnUserResponseModel() {
        String userId = UUID.randomUUID().toString();

        // Mocking the existing user in the repository
        User existingUser = User.builder()
                .userId(userId)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .roles(List.of("Customer"))
                .permissions(List.of("read:countries"))
                .build();

        // Creating expected response model
        UserResponseModel expectedUserResponse = UserResponseModel.builder()
                .userId(userId)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .roles(List.of("Customer"))
                .permissions(List.of("read:countries"))
                .build();

        // Mocking repository behavior
        when(userRepository.findByUserId(userId)).thenReturn(Mono.just(existingUser));

        // Verifying the behavior of the service method
        StepVerifier.create(userService.getUserByUserId(userId))
                .expectNextMatches(response ->
                        response.getUserId().equals(userId) &&
                                response.getEmail().equals("test@example.com") &&
                                response.getFirstName().equals("John") &&
                                response.getLastName().equals("Doe") &&
                                response.getRoles().contains("Customer")
                )
                .verifyComplete();
    }



}