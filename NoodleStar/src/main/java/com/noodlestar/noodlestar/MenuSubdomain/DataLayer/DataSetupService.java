package com.noodlestar.noodlestar.MenuSubdomain.DataLayer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class DataSetupService implements CommandLineRunner {

private final MenuRepository menuRepository;

    @Override
    public void run(String... args) throws Exception {
        setupMenu();
    }

    private void setupMenu() {
        Menu menu1 = buildMenu("menuId1", "Spaghetti", "Delicious pasta with marinara sauce", 12.99, "Main Course", "https://u3t7s5p3.rocketcdn.me/wp-content/uploads/2023/12/Mushroom-Bolognese-585x585.jpg ", Status.AVAILABLE);
        Menu menu2 = buildMenu("menuId2", "Caesar Salad", "Crisp romaine lettuce with Caesar dressing", 7.99, "Appetizer", "https://www.yourultimatemenu.com/wp-content/uploads/2020/09/Ultimate-Caesar-Salad-1200px.jpg ", Status.AVAILABLE);
        Menu menu3 = buildMenu("menuId3", "Margherita Pizza", "Tomato, mozzarella, and fresh basil", 10.99, "Main Course", "https://cdn.loveandlemons.com/wp-content/uploads/2019/09/margherita-pizza.jpg", Status.AVAILABLE);
        Menu menu4 = buildMenu("menuId4", "Cheesecake", "Classic New York-style cheesecake", 5.99, "Dessert", "https://www.simplyrecipes.com/thmb/yFqFfLR03QG0cuAi5WgUSYPrh8Y=/5762x0/filters:no_upscale():max_bytes(150000):strip_icc()/Simply-Recipes-Perfect-Cheesecake-LEAD-6-97a8cb3a60c24903b883c1d5fb5a69d3.jpg ", Status.NOT_AVAILABLE);
        Menu menu5 = buildMenu("menuId5", "Minestrone Soup", "A hearty vegetable soup", 6.99, "Appetizer", "https://www.ambitiouskitchen.com/wp-content/uploads/2020/02/Classic-Minestrone-Soup-1-1007x1536.jpg ", Status.NOT_AVAILABLE);


        Flux.just(menu1, menu2, menu3, menu4, menu5)
                .flatMap(menuRepository::insert)
                .subscribe();
    }

    private Menu buildMenu(String menuId, String name, String description, Double price, String category, String itemImage, Status status) {
        Menu menu = new Menu();
        menu.setMenuId(menuId);
        menu.setName(name);
        menu.setDescription(description);
        menu.setPrice(price);
        menu.setCategory(category);
        menu.setItemImage(itemImage);
        menu.setStatus(status);
        return menu;
    }

}
