package com.noodlestar.noodlestar.MenuSubdomain.utils;

import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Menu;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Status;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuRequestModel;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
import com.noodlestar.noodlestar.utils.EntityDTOUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityDTOUtilTest {

    @Test
    void testToMenuEntity() {
        // Arrange
        MenuRequestModel request = MenuRequestModel.builder()
                .name("Burger")
                .description("A delicious beef burger")
                .price(9.99)
                .category("Fast Food")
                .itemImage("https://example.com/burger.jpg")
                .status(Status.AVAILABLE)
                .build();

        // Act
        Menu result = EntityDTOUtil.toMenuEntity(request);

        // Assert
        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getPrice(), result.getPrice());
        assertEquals(request.getCategory(), result.getCategory());
        assertEquals(request.getItemImage(), result.getItemImage());
        assertEquals(request.getStatus(), result.getStatus());
    }

    @Test
    void testMenuEntityToResponseDTO() {
        // Arrange
        Menu menu = Menu.builder()
                .menuId("menu123")
                .name("Pizza")
                .description("Cheese and tomato pizza")
                .price(14.99)
                .category("Italian")
                .itemImage("https://example.com/pizza.jpg")
                .status(Status.AVAILABLE)
                .build();

        // Act
        MenuResponseModel result = EntityDTOUtil.menuEntityToResponseDTO(menu);

        // Assert
        assertNotNull(result);
        assertEquals(menu.getMenuId(), result.getMenuId());
        assertEquals(menu.getName(), result.getName());
        assertEquals(menu.getDescription(), result.getDescription());
        assertEquals(menu.getPrice(), result.getPrice());
        assertEquals(menu.getCategory(), result.getCategory());
        assertEquals(menu.getItemImage(), result.getItemImage());
        assertEquals(menu.getStatus(), result.getStatus());
    }

    @Test
    void testGenerateMenuIdString() {
        // Act
        String menuId1 = EntityDTOUtil.generateMenuIdString();
        String menuId2 = EntityDTOUtil.generateMenuIdString();

        // Assert
        assertNotNull(menuId1);
        assertNotNull(menuId2);
        assertNotEquals(menuId1, menuId2); // Ensure unique IDs
    }

    @Test
    void testMenuRequestDtoToEntity() {
        // Arrange
        MenuRequestModel dto = MenuRequestModel.builder()
                .name("Sushi")
                .description("Fresh sushi platter")
                .price(19.99)
                .category("Japanese")
                .itemImage("https://example.com/sushi.jpg")
                .status(Status.AVAILABLE)
                .build();

        // Act
        Menu result = EntityDTOUtil.menuRequestDtoToEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getPrice(), result.getPrice());
        assertEquals(dto.getCategory(), result.getCategory());
        assertEquals(dto.getItemImage(), result.getItemImage());
        assertEquals(dto.getStatus(), result.getStatus());
    }
}
