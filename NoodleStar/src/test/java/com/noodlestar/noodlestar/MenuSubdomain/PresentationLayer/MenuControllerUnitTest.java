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


}