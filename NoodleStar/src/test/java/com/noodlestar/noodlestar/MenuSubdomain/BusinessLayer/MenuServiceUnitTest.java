package com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Menu;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.MenuRepository;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Status;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuRequestModel;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
import com.noodlestar.noodlestar.MenuSubdomain.utils.exceptions.NotFoundException;
import com.noodlestar.noodlestar.MenuSubdomain.utils.exceptions.DishNameAlreadyExistsException;
import com.noodlestar.noodlestar.MenuSubdomain.utils.exceptions.InvalidDishDescriptionException;
import com.noodlestar.noodlestar.MenuSubdomain.utils.exceptions.InvalidDishNameException;
import com.noodlestar.noodlestar.MenuSubdomain.utils.exceptions.InvalidDishPriceException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceUnitTest {

    @InjectMocks
    private MenuServiceImpl menuService;

    @Mock
    private MenuRepository menuRepository;

    //#region Dummy data
    Menu menu1 = Menu.builder()
            .id(UUID.randomUUID().toString())
            .menuId(UUID.randomUUID().toString())
            .name("Spaghetti Carbonara")
            .description("Classic Italian pasta with egg, cheese, pancetta, and pepper.")
            .price(12.99)
            .category("Pasta")
            .itemImage("https://www.recipetineats.com/tachyon/2016/09/Wontons_4.jpg")
            .status(Status.AVAILABLE)
            .build();

    Menu menu2 = Menu.builder()
            .id(UUID.randomUUID().toString())
            .menuId(UUID.randomUUID().toString())
            .name("Spaghetti Carbonara2")
            .description("Classic Italian pasta with egg, cheese, pancetta, and pepper.")
            .price(12.99)
            .category("Pasta")
            .itemImage("https://www.recipetineats.com/tachyon/2016/09/Wontons_4.jpg")
            .status(Status.AVAILABLE)
            .build();

    MenuRequestModel menuRequest = MenuRequestModel.builder()
            .name("Tacos")
            .description("Mexican dish with soft tortilla and fillings.")
            .price(8.99)
            .category("Mexican")
            .itemImage("https://www.recipetineats.com/tachyon/2016/09/Wontons_4.jpg")
            .build();

    Menu menuEntity = Menu.builder()
            .id(UUID.randomUUID().toString())
            .menuId(UUID.randomUUID().toString())
            .name("Tacos")
            .description("Mexican dish with soft tortilla and fillings.")
            .price(8.99)
            .category("Mexican")
            .itemImage("https://www.recipetineats.com/tachyon/2016/09/Wontons_4.jpg")
            .status(Status.AVAILABLE)
            .build();
    MenuResponseModel menuResponse = MenuResponseModel.builder()
            .menuId(menuEntity.getMenuId())
            .name(menuEntity.getName())
            .description(menuEntity.getDescription())
            .price(menuEntity.getPrice())
            .category(menuEntity.getCategory())
            .itemImage(menuEntity.getItemImage())
            .status(menuEntity.getStatus())
            .build();
//#endregion



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

    @Test
    void whenAddDish_thenReturnResponseModel() {
        // Arrange
        when(menuRepository.insert(any(Menu.class)))
                .thenReturn(Mono.just(menuEntity));

        // Act
        Mono<MenuResponseModel> resultMono = menuService.addDish(Mono.just(menuRequest));

        // Assert
        MenuResponseModel result = resultMono.block();
        assertNotNull(result);
        assertEquals(menuEntity.getMenuId(), result.getMenuId());
        assertEquals(menuEntity.getName(), result.getName());
        assertEquals(menuEntity.getDescription(), result.getDescription());
        assertEquals(menuEntity.getPrice(), result.getPrice());
        assertEquals(menuEntity.getCategory(), result.getCategory());
        assertEquals(menuEntity.getItemImage(), result.getItemImage());
        assertEquals(menuEntity.getStatus(), result.getStatus());

        verify(menuRepository, times(1)).insert(any(Menu.class));
    }

    @Test
    void whenDishNameIsNull_thenThrowInvalidDishNameException() {
        // Arrange
        MenuRequestModel invalidRequest = MenuRequestModel.builder()
                .name(null)
                .description("A delicious dish")
                .price(10.99)
                .category("Main")
                .itemImage("https://example.com/image.jpg")
                .build();

        // Act & Assert
        Mono<MenuResponseModel> resultMono = menuService.addDish(Mono.just(invalidRequest));
        InvalidDishNameException exception = assertThrows(
                InvalidDishNameException.class,
                resultMono::block
        );

        assertEquals("Dish name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void whenDishNameIsEmpty_thenThrowInvalidDishNameException() {
        // Arrange
        MenuRequestModel invalidRequest = MenuRequestModel.builder()
                .name("")
                .description("A delicious dish")
                .price(10.99)
                .category("Main")
                .itemImage("https://example.com/image.jpg")
                .build();

        // Act & Assert
        Mono<MenuResponseModel> resultMono = menuService.addDish(Mono.just(invalidRequest));
        InvalidDishNameException exception = assertThrows(
                InvalidDishNameException.class,
                resultMono::block
        );

        assertEquals("Dish name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void whenDishPriceIsNull_thenThrowInvalidDishPriceException() {
        // Arrange
        MenuRequestModel invalidRequest = MenuRequestModel.builder()
                .name("Delicious Dish")
                .description("A delicious dish")
                .price(null)
                .category("Main")
                .itemImage("https://example.com/image.jpg")
                .build();

        // Act & Assert
        Mono<MenuResponseModel> resultMono = menuService.addDish(Mono.just(invalidRequest));
        InvalidDishPriceException exception = assertThrows(
                InvalidDishPriceException.class,
                resultMono::block
        );

        assertEquals("Dish price must be greater than 0.", exception.getMessage());
    }

    @Test
    void whenDishPriceIsZeroOrLess_thenThrowInvalidDishPriceException() {
        // Arrange
        MenuRequestModel invalidRequest = MenuRequestModel.builder()
                .name("Delicious Dish")
                .description("A delicious dish")
                .price(0.0)
                .category("Main")
                .itemImage("https://example.com/image.jpg")
                .build();

        // Act & Assert
        Mono<MenuResponseModel> resultMono = menuService.addDish(Mono.just(invalidRequest));
        InvalidDishPriceException exception = assertThrows(
                InvalidDishPriceException.class,
                resultMono::block
        );

        assertEquals("Dish price must be greater than 0.", exception.getMessage());
    }

    @Test
    void whenDishDescriptionIsNull_thenThrowInvalidDishDescriptionException() {
        // Arrange
        MenuRequestModel invalidRequest = MenuRequestModel.builder()
                .name("Delicious Dish")
                .description(null)
                .price(10.99)
                .category("Main")
                .itemImage("https://example.com/image.jpg")
                .build();

        // Act & Assert
        Mono<MenuResponseModel> resultMono = menuService.addDish(Mono.just(invalidRequest));
        InvalidDishDescriptionException exception = assertThrows(
                InvalidDishDescriptionException.class,
                resultMono::block
        );

        assertEquals("Dish description cannot be null or empty.", exception.getMessage());
    }

    @Test
    void whenDishDescriptionIsEmpty_thenThrowInvalidDishDescriptionException() {
        // Arrange
        MenuRequestModel invalidRequest = MenuRequestModel.builder()
                .name("Delicious Dish")
                .description("")
                .price(10.99)
                .category("Main")
                .itemImage("https://example.com/image.jpg")
                .build();

        // Act & Assert
        Mono<MenuResponseModel> resultMono = menuService.addDish(Mono.just(invalidRequest));
        InvalidDishDescriptionException exception = assertThrows(
                InvalidDishDescriptionException.class,
                resultMono::block
        );

        assertEquals("Dish description cannot be null or empty.", exception.getMessage());
    }

}