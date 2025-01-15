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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    public void whenGetStaff_thenReturnStaffResponseModels() {
        // Creating test data for staff users
        User staffUser1 = User.builder()
                .userId(UUID.randomUUID().toString())
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .roles(List.of("Staff"))
                .permissions(List.of("read:staff"))
                .build();

        User staffUser2 = User.builder()
                .userId(UUID.randomUUID().toString())
                .email("jane.doe@example.com")
                .firstName("Jane")
                .lastName("Doe")
                .roles(List.of("Staff"))
                .permissions(List.of("read:staff"))
                .build();

        // Creating the expected response models
        UserResponseModel staffResponseModel1 = UserResponseModel.builder()
                .userId(staffUser1.getUserId())
                .email(staffUser1.getEmail())
                .firstName(staffUser1.getFirstName())
                .lastName(staffUser1.getLastName())
                .roles(staffUser1.getRoles())
                .permissions(staffUser1.getPermissions())
                .build();

        UserResponseModel staffResponseModel2 = UserResponseModel.builder()
                .userId(staffUser2.getUserId())
                .email(staffUser2.getEmail())
                .firstName(staffUser2.getFirstName())
                .lastName(staffUser2.getLastName())
                .roles(staffUser2.getRoles())
                .permissions(staffUser2.getPermissions())
                .build();

        // Mocking the behavior of the repository to return staff users
        when(userRepository.findAll()).thenReturn(Flux.just(staffUser1, staffUser2));

        // Verifying the behavior of the service method
        StepVerifier.create(userService.getStaff())
                .expectNextMatches(response ->
                        response.getUserId().equals(staffResponseModel1.getUserId()) &&
                                response.getEmail().equals(staffResponseModel1.getEmail()) &&
                                response.getFirstName().equals(staffResponseModel1.getFirstName()) &&
                                response.getLastName().equals(staffResponseModel1.getLastName()) &&
                                response.getRoles().equals(staffResponseModel1.getRoles()) &&
                                response.getPermissions().equals(staffResponseModel1.getPermissions()))
                .expectNextMatches(response ->
                        response.getUserId().equals(staffResponseModel2.getUserId()) &&
                                response.getEmail().equals(staffResponseModel2.getEmail()) &&
                                response.getFirstName().equals(staffResponseModel2.getFirstName()) &&
                                response.getLastName().equals(staffResponseModel2.getLastName()) &&
                                response.getRoles().equals(staffResponseModel2.getRoles()) &&
                                response.getPermissions().equals(staffResponseModel2.getPermissions()))
                .verifyComplete();

        // Verifying the repository method call
        verify(userRepository, times(1)).findAll();
    }

}