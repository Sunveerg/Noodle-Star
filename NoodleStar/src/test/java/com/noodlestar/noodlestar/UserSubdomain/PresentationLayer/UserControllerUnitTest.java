package com.noodlestar.noodlestar.UserSubdomain.PresentationLayer;

import com.noodlestar.noodlestar.UserSubdomain.BusinessLayer.UserService;
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

}

