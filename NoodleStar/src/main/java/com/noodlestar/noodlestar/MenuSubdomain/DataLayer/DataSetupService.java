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
        Menu menu1 = buildMenu("menuId1", "Fried Rice", "Stir-frying rice with eggs and seafood.", 11.99, "Main Course", "https://cookingmadehealthy.com/wp-content/uploads/2021/03/Ratio-1-1-Baked-Chinese-Honey-Chicken-640x640.jpg", Status.AVAILABLE);
        Menu menu2 = buildMenu("menuId2", "Won Soup", "Dumplings filled with seasoned pork.", 5.99, "Appetizer", "https://png.pngtree.com/png-vector/20241120/ourlarge/pngtree-traditional-chinese-food-vector-illustration-png-image_14496318.png", Status.AVAILABLE);
        Menu menu3 = buildMenu("menuId3", "Fu Pot", "A seafood pot.", 15.99, "Main Course", "https://png.pngtree.com/png-vector/20241118/ourlarge/pngtree-chinese-food-on-white-background-png-image_14463713.png", Status.AVAILABLE);
        Menu menu4 = buildMenu("menuId4", "Pad Thai", "Thai Stir-fried noodle dish.", 15.99, "Main Course", "https://png.pngtree.com/png-vector/20240901/ourlarge/pngtree-chinese-food-kung-pao-chicken-in-transparent-background-png-image_13705724.png", Status.NOT_AVAILABLE);
        Menu menu5 = buildMenu("menuId5", "Kung Pao Chicken", "A spicy and savory Sichuan.", 12.99, "Main Course", "https://png.pngtree.com/png-vector/20241120/ourlarge/pngtree-chinese-food-vector-illustration-design-png-image_14496321.png", Status.NOT_AVAILABLE);

        Menu menu6 = buildMenu("menuId6", "Spring Rolls",
                "A filling of vegetables.",
                4.99, "Appetizer",
                "https://lh3.googleusercontent.com/xX6_jY_mEcWapvchfiaDLLGrW6xpEB89M5KvyGMYg5pDAY8EqMygoQmQjBanf90pgouj000fIAcjxIBq7A3Jo_xJVp-64W442g=w640-h640-c-rw-v1-e365",
                Status.AVAILABLE);

        Menu menu7 = buildMenu("menuId7", "Sichuan Fish",
                "Fish cooked in a hot sauce.",
                18.99, "Main Course",
                "https://cookingmadehealthy.com/wp-content/uploads/2021/08/Baked-Sesame-Chicken-7-640x640.jpg",
                Status.AVAILABLE);

        Menu menu8 = buildMenu("menuId8", "Beef and Broccoli",
                "Chinese stir-fry dish.",
                13.49, "Main Course",
                "https://cookingmadehealthy.com/wp-content/uploads/2021/08/Healthy-Lemon-Chicken-Stir-Fry-10-640x640.jpg",
                Status.AVAILABLE);

        Menu menu9 = buildMenu("menuId9", "Dumplings",
                "Chinese dumplings with meat.",
                7.99, "Appetizer",
                "https://cdn.pixabay.com/photo/2024/02/21/06/48/ai-generated-8586854_640.png",
                Status.AVAILABLE);

        Menu menu10 = buildMenu("menuId10", "Mongolian Beef",
                "Stir-fried dish made with beef.",
                14.49, "Main Course",
                "https://www.chilihousesf.com/wp-content/uploads/2020/07/24091392.jpg",
                Status.NOT_AVAILABLE);

        Flux.just(menu1, menu2, menu3, menu4, menu5, menu6, menu7, menu8, menu9, menu10)
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
