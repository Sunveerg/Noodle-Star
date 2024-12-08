package com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer;

import com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer.MenuService;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;


import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class MenuControllerUnitTest {
    @Mock
    private MenuService menuService;

    @InjectMocks
    private MenuController menuController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        webTestClient = WebTestClient.bindToController(menuController).build(); // Bind controller manually
    }



    @Test
    void getAllMenu() {
        // Create mock data
        MenuResponseModel menu1 = MenuResponseModel.builder()
                .menuId("1")
                .name("Pizza")
                .description("Cheese and tomato pizza")
                .price(10.99)
                .category("Main Course")
                .itemImage("pizza.jpg")
                .status(Status.AVAILABLE)
                .build();

        MenuResponseModel menu2 = MenuResponseModel.builder()
                .menuId("2")
                .name("Burger")
                .description("Beef burger with lettuce")
                .price(8.99)
                .category("Main Course")
                .itemImage("burger.jpg")
                .status(Status.NOT_AVAILABLE)
                .build();

        // Mock the service to return the mock data
        when(menuService.getAllMenu()).thenReturn(Flux.just(menu1, menu2));

        // Perform the actual test using WebTestClient
        webTestClient.get()
                .uri("/api/v1/menu")  // URI to the controller endpoint
                .accept(MediaType.TEXT_EVENT_STREAM)  // Accept JSON response
                .exchange()
                .expectStatus().isOk()  // Assert that the status is OK
                .expectBodyList(MenuResponseModel.class)  // Assert that the response body is a list of MenuResponseModel
                .hasSize(2)  // Assert that the list has exactly 2 items
                .contains(
                        new MenuResponseModel("1", "Pizza", "Cheese and tomato pizza", 10.99, "Main Course", "pizza.jpg",Status.AVAILABLE),
                        new MenuResponseModel("2", "Burger", "Beef burger with lettuce", 8.99, "Main Course", "burger.jpg",Status.NOT_AVAILABLE)
                );

        // Verify that the service method was called once
        verify(menuService, times(1)).getAllMenu();
    }

    @Test
    void getMenuById() {
        // Arrange
        String menuId = "1";
        MenuResponseModel menu = MenuResponseModel.builder()
                .menuId(menuId)
                .name("Pizza")
                .description("Cheese and tomato pizza")
                .price(10.99)
                .category("Main Course")
                .itemImage("pizza.jpg")
                .status(Status.AVAILABLE)
                .build();

        when(menuService.getMenuById(menuId)).thenReturn(Mono.just(menu));

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/menu/{menuId}", menuId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MenuResponseModel.class)
                .value(response -> {
                    assertEquals(menu.getMenuId(), response.getMenuId());
                    assertEquals(menu.getName(), response.getName());
                    assertEquals(menu.getDescription(), response.getDescription());
                    assertEquals(menu.getPrice(), response.getPrice());
                    assertEquals(menu.getCategory(), response.getCategory());
                    assertEquals(menu.getItemImage(), response.getItemImage());
                    assertEquals(menu.getStatus(), response.getStatus());
                });

        // Verify
        verify(menuService, times(1)).getMenuById(menuId);
    }

    @Test
    void getMenuByIdNotFound() {
        // Arrange
        String menuId = "nonExistingId";
        when(menuService.getMenuById(menuId)).thenReturn(Mono.empty());

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/menu/{menuId}", menuId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateMenu_ValidRequest_ShouldReturnOk() {
        // Arrange
        String menuId = "1";
        MenuRequestModel menuRequestModel = MenuRequestModel.builder()
                .name("Updated Pizza")
                .description("Updated description")
                .price(12.99)
                .category("Main Course")
                .ItemImage("updated_pizza.jpg")
                .status(Status.AVAILABLE)
                .build();

        MenuResponseModel updatedMenu = MenuResponseModel.builder()
                .menuId(menuId)
                .name("Updated Pizza")
                .description("Updated description")
                .price(12.99)
                .category("Main Course")
                .ItemImage("updated_pizza.jpg")
                .status(Status.AVAILABLE)
                .build();

        when(menuService.updateMenu(any(Mono.class), eq(menuId)))
                .thenReturn(Mono.just(updatedMenu));

        // Act and Assert
        webTestClient.put()
                .uri("/api/v1/menu/{menuId}", menuId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(menuRequestModel) // WebTestClient automatically wraps the body in a Mono
                .exchange()
                .expectStatus().isOk()
                .expectBody(MenuResponseModel.class)
                .value(response -> {
                    assertNotNull(response);
                    assertEquals(updatedMenu.getMenuId(), response.getMenuId());
                    assertEquals(updatedMenu.getName(), response.getName());
                    assertEquals(updatedMenu.getDescription(), response.getDescription());
                    assertEquals(updatedMenu.getPrice(), response.getPrice());
                    assertEquals(updatedMenu.getCategory(), response.getCategory());
                    assertEquals(updatedMenu.getItemImage(), response.getItemImage());
                    assertEquals(updatedMenu.getStatus(), response.getStatus());
                });

        // Verify
        verify(menuService, times(1))
                .updateMenu(any(Mono.class), eq(menuId));
    }

    @Test
    void updateMenu_NonExistentMenu_ShouldReturnNotFound() {
        // Arrange
        String nonExistentMenuId = "999";
        MenuRequestModel menuRequestModel = MenuRequestModel.builder()
                .name("Non-existent Item")
                .description("This menu does not exist")
                .price(15.99)
                .category("Main Course")
                .ItemImage("non_existent.jpg")
                .status(Status.NOT_AVAILABLE)
                .build();

        when(menuService.updateMenu(any(Mono.class), eq(nonExistentMenuId)))
                .thenReturn(Mono.empty());

        // Act and Assert
        webTestClient.put()
                .uri("/api/v1/menu/{menuId}", nonExistentMenuId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(menuRequestModel) // WebTestClient automatically wraps the body in a Mono
                .exchange()
                .expectStatus().isNotFound(); // Expect a 404 Not Found status

        // Verify
        verify(menuService, times(1))
                .updateMenu(any(Mono.class), eq(nonExistentMenuId));
    }


}