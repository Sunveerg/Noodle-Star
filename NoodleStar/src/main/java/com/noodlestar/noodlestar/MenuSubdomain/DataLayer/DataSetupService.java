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
        Menu menu1 = buildMenu("menuId1", "Fried Rice", "Fried rice is a Chinese dish made by stir-frying rice with vegetables, eggs, and with seafood, seasoned with soy sauce and spices.", 11.99, "Main Course", "https://cdn.sanity.io/images/2r0kdewr/production/2c90f6410d47972d8555dd5ddcbcc47346957d43-1000x668.jpg", Status.AVAILABLE);
        Menu menu2 = buildMenu("menuId2", "Wonton Soup", "Wonton soup is a Chinese dish with dumplings filled with seasoned pork or shrimp in a light, savory broth, often garnished with green onions.", 5.99, "Appetizer", "https://www.recipetineats.com/tachyon/2016/09/Wontons_4.jpg", Status.AVAILABLE);
        Menu menu3 = buildMenu("menuId3", "Seafood Pot", "A seafood pot is a hearty dish featuring a variety of seafood like shrimp, crab, clams, and mussels, cooked together in a flavorful broth with seasonings, garlic, and vegetables.", 15.99, "Main Course", "https://i.ytimg.com/vi/1Tms0G9io9Y/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLDTucyABzdIuIB84bsAJKKaRyqGZw", Status.AVAILABLE);
        Menu menu4 = buildMenu("menuId4", "Pad Thai", "Pad Thai is a popular Thai stir-fried noodle dish made with rice noodles, eggs, tofu or shrimp, and vegetables, tossed in a tangy, sweet-savory sauce of tamarind, fish sauce, and lime, then topped with fresh herbs.", 15.99, "Main Course", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS3jZGpkzpb6nuIzfe5FNluzCuEBEA2WEwEvw&s", Status.NOT_AVAILABLE);
        Menu menu5 = buildMenu("menuId5", "Kung Pao Chicken", "A spicy and savory Sichuan classic made with tender chicken, stir-fried with peanuts, diced vegetables, and dried chilies in a rich, tangy sauce. Its balanced heat and crunch make it a favorite for bold flavor seekers.", 12.99, "Main Course", "https://www.kitchensanctuary.com/wp-content/uploads/2019/10/Kung-Pao-Chicken-square-FS-39-new.jpg", Status.NOT_AVAILABLE);
        Menu menu6 = buildMenu("menuId6", "Sweet and Sour Pork", "This iconic Cantonese dish features crispy-fried pork pieces coated in a vibrant, glossy sauce combining the sweetness of pineapple and the tang of vinegar, complemented by bell peppers and onions.", 14.99, "Main Course", "https://www.seriouseats.com/thmb/AaMf6tliWc3jh0R-9KLiJG5fzZo=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/__opt__aboutcom__coeus__resources__content_migration__serious_eats__seriouseats.com__2014__07__2021-02-12-Take-Out-Kung-Pao-Chicken-MHOM-11-c46f6c06713c45c5a287ddbf08779d04.jpg", Status.AVAILABLE);
        Menu menu7 = buildMenu("menuId7", "Mapo Tofu", "A Sichuan specialty of silken tofu cubes and ground pork (optional), simmered in a fiery, numbing sauce of chili bean paste, garlic, and fermented black beans, delivering deep umami and heat.", 11.99, "Main Course", "https://cdn.apartmenttherapy.info/image/upload/f_jpg,q_auto:eco,c_fill,g_auto,w_1500,ar_4:3/k%2F2023-05-mapo-tofu%2Fmapo-tofu-017", Status.AVAILABLE);
        Menu menu8 = buildMenu("menuId8", "Beef Chow Fun", "A comforting stir-fried noodle dish with wide rice noodles, tender slices of beef, bean sprouts, and scallions, all tossed in a smoky soy-based sauce. Perfect for noodle lovers.", 13.99, "Main Course", "https://unboundwellness.com/wp-content/uploads/2023/08/beef_chow_fun_1.jpg", Status.NOT_AVAILABLE);
        Menu menu9 = buildMenu("menuId9", "Peking Duck", "Crispy, roasted duck with golden skin, served with thin pancakes, scallions, cucumber slices, and hoisin sauce. This dish is a luxurious feast and a symbol of Chinese culinary finesse.", 28.99, "Main Course", "https://lakegenevacountrymeats.com/wp-content/uploads/Peking-Duck.jpg", Status.NOT_AVAILABLE);
        Menu menu10 = buildMenu("menuId10", "Dim Sum Platter", "An assortment of steamed and fried delicacies, including dumplings, buns, and spring rolls, filled with flavors ranging from shrimp and pork to mushrooms and vegetables. Perfect for sharing!", 18.99, "Main Course", "https://img.freepik.com/premium-photo/colorful-chinese-dim-sum-platter_974629-1764.jpg", Status.NOT_AVAILABLE);


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
