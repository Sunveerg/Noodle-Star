package com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Menu;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.MenuRepository;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Status;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuRequestModel;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
import com.noodlestar.noodlestar.MenuSubdomain.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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

    @Test
    public void whenGetMenuById_thenReturnMenu() {
        // Arrange
        String menuId = menu1.getMenuId();
        when(menuRepository.findMenuByMenuId(menuId)).thenReturn(Mono.just(menu1));

        // Act
        Mono<MenuResponseModel> result = menuService.getMenuById(menuId);

        // Assert
        StepVerifier
                .create(result)
                .assertNext(menuResponseModel -> {
                    assertNotNull(menuResponseModel);
                    assertEquals(menu1.getMenuId(), menuResponseModel.getMenuId());
                    assertEquals(menu1.getName(), menuResponseModel.getName());
                    assertEquals(menu1.getDescription(), menuResponseModel.getDescription());
                    assertEquals(menu1.getPrice(), menuResponseModel.getPrice());
                    assertEquals(menu1.getStatus(), menuResponseModel.getStatus());
                })
                .verifyComplete();
    }

    @Test
    public void whenGetMenuByIdNotFound_thenReturnEmpty() {
        // Arrange
        String menuId = "nonExistingId";
        when(menuRepository.findMenuByMenuId(menuId)).thenReturn(Mono.empty());

        // Act
        Mono<MenuResponseModel> result = menuService.getMenuById(menuId);

        // Assert
        StepVerifier
                .create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void whenUpdateMenu_thenReturnUpdatedMenu() {
        // Arrange
        String menuId = menu1.getMenuId();
        MenuRequestModel menuRequestModel = new MenuRequestModel();
        menuRequestModel.setName("Updated Spaghetti Carbonara");
        menuRequestModel.setDescription("Updated classic Italian pasta with egg, cheese, pancetta, and pepper.");
        menuRequestModel.setPrice(14.99);
        menuRequestModel.setCategory("Updated Pasta");
        menuRequestModel.setItemImage("updated_spaghetti_carbonara.jpg");
        menuRequestModel.setStatus(Status.AVAILABLE);

        when(menuRepository.findMenuByMenuId(menuId)).thenReturn(Mono.just(menu1));
        when(menuRepository.save(menu1)).thenReturn(Mono.just(menu1));

        // Act
        Mono<MenuResponseModel> result = menuService.updateMenu(Mono.just(menuRequestModel), menuId);

        // Assert
        StepVerifier
                .create(result)
                .assertNext(menuResponseModel -> {
                    assertNotNull(menuResponseModel);
                    assertEquals(menu1.getMenuId(), menuResponseModel.getMenuId());
                    assertEquals("Updated Spaghetti Carbonara", menuResponseModel.getName());
                    assertEquals("Updated classic Italian pasta with egg, cheese, pancetta, and pepper.", menuResponseModel.getDescription());
                    assertEquals(14.99, menuResponseModel.getPrice());
                    assertEquals("Updated Pasta", menuResponseModel.getCategory());
                    assertEquals("updated_spaghetti_carbonara.jpg", menuResponseModel.getItemImage());
                    assertEquals(Status.AVAILABLE, menuResponseModel.getStatus());
                })
                .verifyComplete();
    }

    @Test
    public void whenUpdateMenuNotFound_thenReturnNotFoundException() {
        // Arrange
        String menuId = "nonExistingMenuId";  // Using a non-existing menu ID
        MenuRequestModel menuRequestModel = new MenuRequestModel();
        menuRequestModel.setName("Non-existent Menu");
        menuRequestModel.setDescription("This menu does not exist.");
        menuRequestModel.setPrice(20.99);
        menuRequestModel.setCategory("Non-existent Category");
        menuRequestModel.setItemImage("non_existent_image.jpg");
        menuRequestModel.setStatus(Status.AVAILABLE);

        when(menuRepository.findMenuByMenuId(menuId)).thenReturn(Mono.empty());

        // Act
        Mono<MenuResponseModel> result = menuService.updateMenu(Mono.just(menuRequestModel), menuId);

        // Assert
        StepVerifier
                .create(result)
                .expectError(NotFoundException.class) // Expect a NotFoundException to be thrown
                .verify();
    }

}