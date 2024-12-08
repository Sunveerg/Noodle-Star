package com.noodlestar.noodlestar.MenuSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Menu;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.MenuRepository;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Status;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuRequestModel;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
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
    void whenAddDish_thenReturnResponseModel() {
        // Arrange
        when(menuRepository.findByName(menuRequest.getName()))
                .thenReturn(Mono.empty());
        when(menuRepository.save(any(Menu.class)))
                .thenReturn(Mono.just(menuEntity));

        // Act
        MenuResponseModel result = menuService.addDish(menuRequest);

        // Assert
        verify(menuRepository, times(1)).findByName(menuRequest.getName());
        verify(menuRepository, times(1)).save(argThat(savedMenu -> {
            assertNotNull(savedMenu.getMenuId());
            assertEquals(menuRequest.getName(), savedMenu.getName());
            assertEquals(menuRequest.getDescription(), savedMenu.getDescription());
            assertEquals(menuRequest.getPrice(), savedMenu.getPrice());
            assertEquals(menuRequest.getCategory(), savedMenu.getCategory());
            assertEquals(menuRequest.getItemImage(), savedMenu.getItemImage());
            assertEquals(Status.AVAILABLE, savedMenu.getStatus());
            return true;
        }));

        // Validate the response
        assertNotNull(result.getMenuId());
        assertEquals(menuEntity.getMenuId(), result.getMenuId());
        assertEquals(menuEntity.getName(), result.getName());
        assertEquals(menuEntity.getDescription(), result.getDescription());
        assertEquals(menuEntity.getPrice(), result.getPrice());
        assertEquals(menuEntity.getCategory(), result.getCategory());
        assertEquals(menuEntity.getItemImage(), result.getItemImage());
        assertEquals(menuEntity.getStatus(), result.getStatus());
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
        InvalidDishNameException exception = assertThrows(
                InvalidDishNameException.class,
                () -> menuService.addDish(invalidRequest)
        );

        assertEquals("Dish name cannot be null or empty", exception.getMessage());
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
        InvalidDishNameException exception = assertThrows(
                InvalidDishNameException.class,
                () -> menuService.addDish(invalidRequest)
        );

        assertEquals("Dish name cannot be null or empty", exception.getMessage());
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
        InvalidDishPriceException exception = assertThrows(
                InvalidDishPriceException.class,
                () -> menuService.addDish(invalidRequest)
        );

        assertEquals("Dish price must be greater than 0", exception.getMessage());
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
        InvalidDishPriceException exception = assertThrows(
                InvalidDishPriceException.class,
                () -> menuService.addDish(invalidRequest)
        );

        assertEquals("Dish price must be greater than 0", exception.getMessage());
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
        InvalidDishDescriptionException exception = assertThrows(
                InvalidDishDescriptionException.class,
                () -> menuService.addDish(invalidRequest)
        );

        assertEquals("Dish description cannot be null or empty", exception.getMessage());
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
        InvalidDishDescriptionException exception = assertThrows(
                InvalidDishDescriptionException.class,
                () -> menuService.addDish(invalidRequest)
        );

        assertEquals("Dish description cannot be null or empty", exception.getMessage());
    }

    @Test
    void whenDishNameAlreadyExists_thenThrowDishNameAlreadyExistsException() {
        // Arrange
        when(menuRepository.findByName(menuRequest.getName())).thenReturn(Mono.just(menuEntity));

        // Act & Assert
        DishNameAlreadyExistsException exception = assertThrows(
                DishNameAlreadyExistsException.class,
                () -> menuService.addDish(menuRequest)
        );

        assertEquals("Dish with name 'Tacos' already exists.", exception.getMessage());

        verify(menuRepository, times(1)).findByName(menuRequest.getName());
        verify(menuRepository, never()).save(any(Menu.class));
    }


}