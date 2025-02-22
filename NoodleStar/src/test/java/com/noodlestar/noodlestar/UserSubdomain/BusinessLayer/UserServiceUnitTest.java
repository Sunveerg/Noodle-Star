package com.noodlestar.noodlestar.UserSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.UserSubdomain.DataLayer.User;
import com.noodlestar.noodlestar.UserSubdomain.DataLayer.UserRepository;
import com.noodlestar.noodlestar.UserSubdomain.PresentationLayer.UserRequestModel;
import com.noodlestar.noodlestar.UserSubdomain.PresentationLayer.UserResponseModel;
import com.noodlestar.noodlestar.auth0.Auth0Service;
import com.noodlestar.noodlestar.utils.EntityDTOUtil;
import com.noodlestar.noodlestar.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;

import static de.flapdoodle.os.Platform.logger;
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
        when(auth0Service.assignRoleToUser(auth0UserId, "rol_u24yYe5H0NveA454")).thenReturn(Mono.empty());
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
    @Test
    public void whenDeleteStaff_thenDeleteStaff() {
        // Arrange
        String userId = UUID.randomUUID().toString();

        User staffUser = User.builder()
                .userId(userId)
                .email("s")
                .firstName("John")
                .lastName("Doe")
                .roles(List.of("Staff"))
                .permissions(List.of("read:staff"))
                .build();

        when(userRepository.findByUserId(userId)).thenReturn(Mono.just(staffUser));
        when(userRepository.delete(staffUser)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = userService.deleteStaff(userId);

        // Assert
        result.subscribe();

        // Verify
        verify(userRepository, times(1)).findByUserId(userId);
        verify(userRepository, times(1)).delete(staffUser);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    public void whenDeleteStaffNotFound_thenThrowException() {
        // Arrange
        String userId = UUID.randomUUID().toString();

        when(userRepository.findByUserId(userId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(userService.deleteStaff(userId))
                .expectError(NotFoundException.class)
                .verify();

        // Verify
        verify(userRepository, times(1)).findByUserId(userId);
        verify(userRepository, times(0)).delete(any());
    }

    @Test
    void whenUpdateStaffWithValidData_thenReturnUpdatedUserResponseModel() {
        // Arrange
        String userId = UUID.randomUUID().toString();

        User existingStaffUser = User.builder()
                .userId(userId)
                .email("old.email@example.com")
                .firstName("OldFirstName")
                .lastName("OldLastName")
                .roles(List.of("Staff"))
                .permissions(List.of("read:staff"))
                .build();

        UserRequestModel updateRequest = UserRequestModel.builder()
                .email("new.email@example.com")
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .roles(List.of("Staff"))
                .permissions(List.of("write:staff"))
                .build();

        User updatedStaffUser = User.builder()
                .userId(userId)
                .email("new.email@example.com")
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .roles(List.of("Staff"))
                .permissions(List.of("write:staff"))
                .build();

        UserResponseModel expectedResponse = UserResponseModel.builder()
                .userId(userId)
                .email("new.email@example.com")
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .roles(List.of("Staff"))
                .permissions(List.of("write:staff"))
                .build();

        when(userRepository.findByUserId(userId)).thenReturn(Mono.just(existingStaffUser));
        when(userRepository.save(existingStaffUser)).thenReturn(Mono.just(updatedStaffUser));

        // Act & Assert
        StepVerifier.create(userService.updateStaff(Mono.just(updateRequest), userId))
                .expectNextMatches(response ->
                        response.getUserId().equals(expectedResponse.getUserId()) &&
                                response.getEmail().equals(expectedResponse.getEmail()) &&
                                response.getFirstName().equals(expectedResponse.getFirstName()) &&
                                response.getLastName().equals(expectedResponse.getLastName()) &&
                                response.getRoles().equals(expectedResponse.getRoles()) &&
                                response.getPermissions().equals(expectedResponse.getPermissions())
                )
                .verifyComplete();

        // Verify
        verify(userRepository, times(1)).findByUserId(userId);
        verify(userRepository, times(1)).save(existingStaffUser);
    }

    @Test
    void whenUpdateStaffNotFound_thenThrowNotFoundException() {
        // Arrange
        String userId = UUID.randomUUID().toString();

        UserRequestModel updateRequest = UserRequestModel.builder()
                .email("new.email@example.com")
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .roles(List.of("Staff"))
                .permissions(List.of("write:staff"))
                .build();

        when(userRepository.findByUserId(userId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(userService.updateStaff(Mono.just(updateRequest), userId))
                .expectErrorMatches(throwable ->
                        throwable instanceof NotFoundException &&
                                throwable.getMessage().equals("Staff not found with id: " + userId)
                )
                .verify();

        // Verify
        verify(userRepository, times(1)).findByUserId(userId);
        verify(userRepository, times(0)).save(any());
    }

    void whenAddStaffRoleToUser_thenReturnUpdatedUserResponseModel() {
        String userId = UUID.randomUUID().toString();

        // Mock the existing user
        User existingUser = User.builder()
                .userId(userId)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .roles(new ArrayList<>(List.of("Customer"))) // Initially, the user has the "Customer" role
                .permissions(new ArrayList<>(List.of("read:countries")))
                .build();

        // Mock the updated user after assigning "Staff" role
        User updatedUser = User.builder()
                .userId(userId)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .roles(new ArrayList<>(List.of("Customer", "Staff"))) // "Staff" role is added
                .permissions(new ArrayList<>(List.of("read:countries")))
                .build();

        // Create a UserResponseModel for the expected output
        UserResponseModel expectedResponse = UserResponseModel.builder()
                .userId(userId)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .roles(new ArrayList<>(List.of("Customer", "Staff"))) // "Staff" role is included
                .permissions(new ArrayList<>(List.of("read:countries")))
                .build();

        // Mock repository and service interactions
        when(userRepository.findByUserId(userId)).thenReturn(Mono.just(existingUser));
        when(auth0Service.assignRoleToUser(userId, "rol_u24yYe5H0NveA454")).thenReturn(Mono.empty()); // Simulate successful role assignment
        when(userRepository.save(updatedUser)).thenReturn(Mono.just(updatedUser)); // Simulate saving the updated user

        // Act and Assert: Use StepVerifier to validate the outcome
        StepVerifier.create(userService.addStaffRoleToUser(userId))
                .expectNextMatches(response ->
                        response.getUserId().equals(userId) &&
                                response.getRoles().contains("Staff") // Verify that "Staff" role is assigned
                )
                .verifyComplete(); // Ensure the Mono completes successfully

        // Verify interactions with mocks
        verify(userRepository, times(1)).findByUserId(userId); // Ensure the user lookup was performed once
        verify(auth0Service, times(1)).assignRoleToUser(userId, "rol_u24yYe5H0NveA454"); // Ensure role assignment was called
        verify(userRepository, times(1)).save(updatedUser); // Ensure the updated user was saved
    }

    @Test
    void whenGetAllUsers_thenReturnUserList() {
    // Given: Mock users to be returned
    User user1 = new User();
    user1.setUserId("auth0|675f6ad19a80612ce548e0b2");
    user1.setRoles(List.of("Staff"));

    User user2 = new User();
    user2.setUserId("auth0|675f6ad19a80612ce548e0b3");
    user2.setRoles(List.of("Admin"));

    // Convert to UserResponseModel (DTO)
    UserResponseModel userResponseModel1 = EntityDTOUtil.toUserResponseModel(user1);
    UserResponseModel userResponseModel2 = EntityDTOUtil.toUserResponseModel(user2);

    // Mock the userRepository's findAll() to return a Flux of users
    when(userRepository.findAll()).thenReturn(Flux.just(user1, user2));

    // When / Then: Use StepVerifier to verify the result of getAllUsers()
    StepVerifier.create(userService.getAllUsers())
            .expectNext(userResponseModel1)   // Expect the first user to be mapped
            .expectNext(userResponseModel2)   // Expect the second user to be mapped
            .verifyComplete();               // Verify that the Flux completes without errors

    // Optionally, verify interactions with the mocks (e.g., checking that the repository method was called)
    verify(userRepository, times(1)).findAll();
}


    @Test
    void whenUserNotFound_thenThrowNotFoundException() {
        String userId = "auth0|nonexistent";
        when(userRepository.findByUserId(userId)).thenReturn(Mono.empty());

        StepVerifier.create(userService.addStaffRoleToUser(userId))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException
                        && throwable.getMessage().contains("User not found with ID"))
                .verify();
    }




    @Test
    void whenUpdateUserWithValidData_thenReturnUpdatedUserResponseModel() {
        // Arrange
        String userId = UUID.randomUUID().toString();

        User existingStaffUser = User.builder()
                .userId(userId)
                .email("old.email@example.com")
                .firstName("OldFirstName")
                .lastName("OldLastName")
                .roles(List.of("Customer"))
                .permissions(List.of("read:staff"))
                .build();

        UserRequestModel updateRequest = UserRequestModel.builder()
                .email("new.email@example.com")
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .roles(List.of("Customer"))
                .permissions(List.of("write:staff"))
                .build();

        User updatedStaffUser = User.builder()
                .userId(userId)
                .email("new.email@example.com")
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .roles(List.of("Staff"))
                .permissions(List.of("write:staff"))
                .build();

        UserResponseModel expectedResponse = UserResponseModel.builder()
                .userId(userId)
                .email("new.email@example.com")
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .roles(List.of("Staff"))
                .permissions(List.of("write:staff"))
                .build();

        when(userRepository.findByUserId(userId)).thenReturn(Mono.just(existingStaffUser));
        when(userRepository.save(existingStaffUser)).thenReturn(Mono.just(updatedStaffUser));

        // Act & Assert
        StepVerifier.create(userService.updateUser(Mono.just(updateRequest), userId))
                .expectNextMatches(response ->
                        response.getUserId().equals(expectedResponse.getUserId()) &&
                                response.getEmail().equals(expectedResponse.getEmail()) &&
                                response.getFirstName().equals(expectedResponse.getFirstName()) &&
                                response.getLastName().equals(expectedResponse.getLastName()) &&
                                response.getRoles().equals(expectedResponse.getRoles()) &&
                                response.getPermissions().equals(expectedResponse.getPermissions())
                )
                .verifyComplete();

        // Verify
        verify(userRepository, times(1)).findByUserId(userId);
        verify(userRepository, times(1)).save(existingStaffUser);
    }
}