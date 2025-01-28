package com.noodlestar.noodlestar.UserSubdomain.PresentationLayer;

import com.noodlestar.noodlestar.UserSubdomain.BusinessLayer.UserService;
import com.noodlestar.noodlestar.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerUnitTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        webTestClient = WebTestClient.bindToController(userController).build(); // Bind controller manually
    }

    @Test
    void handleUserLogin() {
        String userId = "test-user-id";

        UserResponseModel userResponseModel = UserResponseModel.builder()
                .userId(userId)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .roles(List.of("Customer"))
                .permissions(List.of("read:countries"))
                .build();

        when(userService.addUserFromAuth0(userId)).thenReturn(Mono.just(userResponseModel));

        // Perform the actual test using WebTestClient
        webTestClient.post()
                .uri("/api/v1/users/{userId}/login", userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk() // Assert that the status is OK
                .expectBody(UserResponseModel.class)
                .isEqualTo(userResponseModel); // Assert that the response body matches

        // Verify that the userService method was called once
        verify(userService, times(1)).addUserFromAuth0(userId);
    }

    @Test
    void syncUser() {
        String userId = "test-user-id";

        UserResponseModel userResponseModel = UserResponseModel.builder()
                .userId(userId)
                .email("updated@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .roles(List.of("Admin"))
                .permissions(List.of("read:countries", "write:countries"))
                .build();

        when(userService.syncUserWithAuth0(userId)).thenReturn(Mono.just(userResponseModel));

        // Perform the actual test using WebTestClient
        webTestClient.put()
                .uri("/api/v1/users/{userId}/sync", userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk() // Assert that the status is OK
                .expectBody(UserResponseModel.class)
                .isEqualTo(userResponseModel); // Assert that the response body matches

        // Verify that the userService method was called once
        verify(userService, times(1)).syncUserWithAuth0(userId);
    }

    @Test
    void getUserByUserId() {
        String userId = "test-user-id";

        UserResponseModel userResponseModel = UserResponseModel.builder()
                .userId(userId)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .roles(List.of("Customer"))
                .permissions(List.of("read:countries"))
                .build();

        when(userService.getUserByUserId(userId)).thenReturn(Mono.just(userResponseModel));

        // Perform the actual test using WebTestClient
        webTestClient.get()
                .uri("/api/v1/users/{userId}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk() // Assert that the status is OK
                .expectBody(UserResponseModel.class)
                .isEqualTo(userResponseModel); // Assert that the response body matches

        // Verify that the userService method was called once
        verify(userService, times(1)).getUserByUserId(userId);
    }

    @Test
    void handleUserLoginNotFound() {
        String userId = "nonexistent-user-id";

        when(userService.addUserFromAuth0(userId)).thenReturn(Mono.empty());

        // Perform the actual test using WebTestClient
        webTestClient.post()
                .uri("/api/v1/users/{userId}/login", userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound() // Assert that the status is 404 Not Found
                .expectBody().isEmpty(); // Assert that the response body is empty

        // Verify that the userService method was called once
        verify(userService, times(1)).addUserFromAuth0(userId);
    }

    @Test
    void syncUserNotFound() {
        String userId = "nonexistent-user-id";

        when(userService.syncUserWithAuth0(userId)).thenReturn(Mono.empty());

        // Perform the actual test using WebTestClient
        webTestClient.put()
                .uri("/api/v1/users/{userId}/sync", userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound() // Assert that the status is 404 Not Found
                .expectBody().isEmpty(); // Assert that the response body is empty

        // Verify that the userService method was called once
        verify(userService, times(1)).syncUserWithAuth0(userId);
    }

    @Test
    void getStaff() {
        // Create sample UserResponseModel objects
        UserResponseModel user1 = UserResponseModel.builder()
                .userId("1")
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .roles(Arrays.asList("Admin", "User"))
                .permissions(Arrays.asList("READ", "WRITE"))
                .build();

        UserResponseModel user2 = UserResponseModel.builder()
                .userId("2")
                .email("jane.doe@example.com")
                .firstName("Jane")
                .lastName("Doe")
                .roles(Arrays.asList("User"))
                .permissions(Arrays.asList("READ"))
                .build();

        when(userService.getStaff()).thenReturn(Flux.just(user1, user2));

        webTestClient.get()
                .uri("/api/v1/users/staff")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponseModel.class)
                .hasSize(2)
                .contains(user1, user2);

        verify(userService, times(1)).getStaff();
    }

    @Test
    void getAllUsers() {
        // Create sample UserResponseModel objects
        UserResponseModel user1 = UserResponseModel.builder()
                .userId("1")
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .roles(Arrays.asList("Admin", "User"))
                .permissions(Arrays.asList("READ", "WRITE"))
                .build();

        UserResponseModel user2 = UserResponseModel.builder()
                .userId("2")
                .email("jane.doe@example.com")
                .firstName("Jane")
                .lastName("Doe")
                .roles(Arrays.asList("User"))
                .permissions(Arrays.asList("READ"))
                .build();

        // Mock the service call
        when(userService.getAllUsers()).thenReturn(Flux.just(user1, user2));

        // Perform the actual test using WebTestClient
        webTestClient.get()
                .uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk() // Assert that the status is OK
                .expectBodyList(UserResponseModel.class)
                .hasSize(2) // Assert the size of the returned list
                .contains(user1, user2); // Assert that both users are returned

        // Verify that the userService method was called once
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void deleteStaff() {
        String userId = "test-user-id";

        when(userService.deleteStaff(userId)).thenReturn(Mono.empty());

        // Perform the actual test using WebTestClient
        webTestClient.delete()
                .uri("/api/v1/users/staff/{userId}", userId)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        // Verify that the userService method was called once
        verify(userService, times(1)).deleteStaff(userId);
    }

    @Test
    void deleteStaffNotFound() {
        String userId = "nonexistent-user-id";

        when(userService.deleteStaff(userId))
                .thenReturn(Mono.error(new NotFoundException("User not found")));

        // Perform the actual test using WebTestClient
        webTestClient.delete()
                .uri("/api/v1/users/staff/{userId}", userId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();

        // Verify that the userService method was called once
        verify(userService, times(1)).deleteStaff(userId);
    }

    @Test
    void updateStaffSuccess() {
        String userId = "staff-user-id";

        UserRequestModel userRequestModel = UserRequestModel.builder()
                .email("updated.staff@example.com")
                .firstName("UpdatedStaff")
                .lastName("User")
                .roles(List.of("Staff"))
                .permissions(List.of("read:staff", "write:staff"))
                .build();

        UserResponseModel updatedUser = UserResponseModel.builder()
                .userId(userId)
                .email(userRequestModel.getEmail())
                .firstName(userRequestModel.getFirstName())
                .lastName(userRequestModel.getLastName())
                .roles(userRequestModel.getRoles())
                .permissions(userRequestModel.getPermissions())
                .build();

        when(userService.updateStaff(any(Mono.class), eq(userId))).thenReturn(Mono.just(updatedUser));

        webTestClient.put()
                .uri("/api/v1/users/staff/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequestModel)
                .exchange()
                .expectStatus().isOk() // Assert 200 OK
                .expectBody(UserResponseModel.class)
                .isEqualTo(updatedUser); // Assert the response matches the updated user details

        verify(userService, times(1)).updateStaff(any(Mono.class), eq(userId));
    }

    @Test
    void updateStaffNotFound() {
        String userId = "nonexistent-user-id";

        UserRequestModel userRequestModel = UserRequestModel.builder()
                .email("nonexistent.staff@example.com")
                .firstName("NonExistent")
                .lastName("User")
                .roles(List.of("Staff"))
                .permissions(List.of("read:staff"))
                .build();

        when(userService.updateStaff(any(Mono.class), eq(userId))).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/api/v1/users/staff/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequestModel)
                .exchange()
                .expectStatus().isNotFound() // Assert 404 Not Found
                .expectBody().isEmpty(); // Assert no response body

        verify(userService, times(1)).updateStaff(any(Mono.class), eq(userId));
    }

    void addStaffMember() {
        String userId = "test-user-id";

        // Assuming the service method returns a UserResponseModel
        UserResponseModel userResponseModel = UserResponseModel.builder()
                .userId(userId)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .roles(Arrays.asList("Customer", "Staff"))
                .permissions(Arrays.asList("read:users", "write:users"))
                .build();

        // Mock the service call for adding the staff role
        when(userService.addStaffRoleToUser(userId)).thenReturn(Mono.just(userResponseModel));

        // Perform the actual test using WebTestClient
        webTestClient.post()
                .uri("/api/v1/users/staff/{userId}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk() // Assert that the status is OK
                .expectBody(UserResponseModel.class)
                .isEqualTo(userResponseModel); // Assert that the response body matches the expected user

        // Verify that the service method was called once
        verify(userService, times(1)).addStaffRoleToUser(userId);
    }

    @Test
    void addStaffMemberNotFound() {
        String userId = "nonexistent-user-id";

        // Mock the service call to simulate a NotFoundException
        when(userService.addStaffRoleToUser(userId)).thenReturn(Mono.error(new NotFoundException("User not found")));

        // Perform the actual test using WebTestClient
        webTestClient.post()
                .uri("/api/v1/users/staff/{userId}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound() // Assert that the status is 404 Not Found
                .expectBody().isEmpty(); // Assert that the response body is empty

        // Verify that the service method was called once
        verify(userService, times(1)).addStaffRoleToUser(userId);
    }

//    @Test
//    void addStaffMemberConflict() {
//        String userId = "test-user-id";
//
//        // Mock the service call to simulate an IllegalStateException
//        when(userService.addStaffRoleToUser(userId)).thenReturn(Mono.error(new IllegalStateException("Invalid operation")));
//
//        // Perform the actual test using WebTestClient
//        webTestClient.post()
//                .uri("/api/v1/users/staff/{userId}", userId)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isConflict() // Assert that the status is 409 Conflict
//                .expectBody().isEmpty(); // Assert that the response body is empty
//
//        // Verify that the service method was called once
//        verify(userService, times(1)).addStaffRoleToUser(userId);
//    }



    @Test
    void updateUserSuccess() {
        String userId = "staff-user-id";

        UserRequestModel userRequestModel = UserRequestModel.builder()
                .email("updated.staff@example.com")
                .firstName("UpdatedStaff")
                .lastName("User")
                .roles(List.of("Customer"))
                .permissions(List.of("read:staff", "write:staff"))
                .build();

        UserResponseModel updatedUser = UserResponseModel.builder()
                .userId(userId)
                .email(userRequestModel.getEmail())
                .firstName(userRequestModel.getFirstName())
                .lastName(userRequestModel.getLastName())
                .roles(userRequestModel.getRoles())
                .permissions(userRequestModel.getPermissions())
                .build();

        when(userService.updateUser(any(Mono.class), eq(userId))).thenReturn(Mono.just(updatedUser));

        webTestClient.put()
                .uri("/api/v1/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequestModel)
                .exchange()
                .expectStatus().isOk() // Assert 200 OK
                .expectBody(UserResponseModel.class)
                .isEqualTo(updatedUser); // Assert the response matches the updated user details

        verify(userService, times(1)).updateUser(any(Mono.class), eq(userId));
    }

}

