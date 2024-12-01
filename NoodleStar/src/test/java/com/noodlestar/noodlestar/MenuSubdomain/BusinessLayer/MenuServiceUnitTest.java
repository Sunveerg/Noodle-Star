package com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Menu;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.MenuRepository;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Status;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceUnitTest {

    @InjectMocks
    private MenuServiceImpl menuService;

    @Mock
    private MenuRepository menuRepository;

    Menu menu1 = Menu.builder()
            .id(UUID.randomUUID().toString())
            .menuId(UUID.randomUUID().toString())
            .name("Spaghetti Carbonara")
            .description("Classic Italian pasta with egg, cheese, pancetta, and pepper.")
            .price(12.99)
            .category("Pasta")
            .ItemImage("spaghetti_carbonara.jpg")
            .status(Status.AVAILABLE)
            .build();

    Menu menu2 = Menu.builder()
            .id(UUID.randomUUID().toString())
            .menuId(UUID.randomUUID().toString())
            .name("Spaghetti Carbonara2")
            .description("Classic Italian pasta with egg, cheese, pancetta, and pepper.")
            .price(12.99)
            .category("Pasta")
            .ItemImage("spaghetti_carbonara.jpg")
            .status(Status.AVAILABLE)
            .build();




    @Test
    public void whenGetAllMenu_thenReturnMenu() {
        // Arrange
        when(menuRepository.findAll()).thenReturn(Flux.just(menu1, menu2));

        // Act
        Flux<MenuResponseModel> result = menuService.getAllMenu();

        // Assert
        StepVerifier
                .create(result)
                .expectNextMatches(menuResponseModel -> menuResponseModel.getMenuId().equals(menu1.getMenuId()))
                .expectNextMatches(menuResponseModel -> menuResponseModel.getMenuId().equals(menu2.getMenuId()))
                .verifyComplete();
    }

}