package com.noodlestar.noodlestar.utils;

import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Menu;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.MenuRepository;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Status;
import com.noodlestar.noodlestar.ReportSubdomain.DataLayer.Report;
import com.noodlestar.noodlestar.ReportSubdomain.DataLayer.ReportRepository;
import com.noodlestar.noodlestar.ReviewSubdomain.DataLayer.Review;
import com.noodlestar.noodlestar.ReviewSubdomain.DataLayer.ReviewRepo;
import com.noodlestar.noodlestar.TeamMemberSubdomain.DataLayer.TeamMember;
import com.noodlestar.noodlestar.TeamMemberSubdomain.DataLayer.TeamMemberRepository;
import com.noodlestar.noodlestar.UserSubdomain.DataLayer.User;
import com.noodlestar.noodlestar.UserSubdomain.DataLayer.UserRepository;
import com.noodlestar.noodlestar.ordersubdomain.datalayer.Order;
import com.noodlestar.noodlestar.ordersubdomain.datalayer.OrderDetails;
import com.noodlestar.noodlestar.ordersubdomain.datalayer.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataSetupServiceReview implements CommandLineRunner {
    private final ReviewRepo reviewRepo;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private  final ReportRepository reportRepo;
    private final TeamMemberRepository teamMemberRepository;


    @Override
    public void run(String... args) throws Exception {
        setupReviews();
        setupMenu();
        setupOrders();
        setupUsers();
        setupReports();
        setupTeamMembers();
    }



    private void setupMenu() {
        List<Menu> menus = List.of(
                buildMenu("menuId6", "Won Ton Soup (Pork)", "Won Ton soup with pork", 4.00, "Appetizers & Soups", "", Status.AVAILABLE),
        buildMenu("menuId7", "Hot & Sour Soup", "A classic hot and sour soup", 4.00, "Appetizers & Soups", "", Status.AVAILABLE),
        buildMenu("menuId8", "Tom Yum Soup with Tofu", "Spicy and sour Tom Yum soup with tofu", 4.00, "Appetizers & Soups", "", Status.AVAILABLE),
        buildMenu("menuId9", "Tom Yum Soup with Shrimp", "Spicy and sour Tom Yum soup with shrimp", 5.00, "Appetizers & Soups", "", Status.AVAILABLE),
        buildMenu("menuId10", "Tom Yum Soup with Seafood", "Spicy and sour Tom Yum soup with seafood", 5.50, "Appetizers & Soups", "", Status.AVAILABLE),
        buildMenu("menuId11", "Seafood Soup", "A flavorful seafood soup", 5.50, "Appetizers & Soups", "", Status.AVAILABLE),
        buildMenu("menuId12", "Chicken & Sweet Corn Soup", "Chicken soup with sweet corn", 4.00, "Appetizers & Soups", "", Status.AVAILABLE),
        buildMenu("menuId13", "Tofu & Sweet Corn Soup", "Tofu soup with sweet corn", 4.00, "Appetizers & Soups", "", Status.AVAILABLE),
        buildMenu("menuId14", "Vegetarian Imperial Rolls (2)", "Crispy vegetarian imperial rolls", 4.00, "Appetizers & Soups", "", Status.AVAILABLE),
        buildMenu("menuId15", "Vietnamese Fried Spring Rolls (2, Pork)", "Crispy Vietnamese-style fried spring rolls with pork", 4.00, "Appetizers & Soups", "", Status.AVAILABLE),
        buildMenu("menuId16", "Peanut Butter Dumplings (Pork)", "Steamed dumplings with a peanut butter sauce", 7.00, "Appetizers & Soups", "", Status.AVAILABLE),
        buildMenu("menuId17", "Fried Wonton (Shrimp & Pork)", "Crispy fried wontons filled with shrimp and pork", 8.00, "Appetizers & Soups", "", Status.AVAILABLE),
        buildMenu("menuId18", "Fried Dumplings (Chicken) (6)", "Crispy pan-fried dumplings with chicken", 7.00, "Appetizers & Soups", "", Status.AVAILABLE),
        buildMenu("menuId19", "Salt & Pepper Calamari Rings", "Crispy calamari rings with salt and pepper seasoning", 9.00, "Appetizers & Soups", "", Status.AVAILABLE),
        buildMenu("menuId20", "Sesame Seed Balls (6)", "Sweet sesame seed-coated rice balls", 3.00, "Appetizers & Soups", "", Status.AVAILABLE),
        buildMenu("menuId21", "Crispy Spinach", "Crispy-fried spinach leaves", 7.00, "Appetizers & Soups", "", Status.AVAILABLE),

                buildMenu("menuId22", "Well-Done Beef Noodles Soup", "Rice noodle soup with well-done beef", 12.50, "Soup & Noodles", "", Status.AVAILABLE),
        buildMenu("menuId23", "Chicken & Vegetables Noodles Soup", "Rice noodle soup with chicken and mixed vegetables", 12.50, "Soup & Noodles", "", Status.AVAILABLE),
        buildMenu("menuId24", "Chicken Noodles Soup", "Rice noodle soup with chicken", 12.50, "Soup & Noodles", "", Status.AVAILABLE),
        buildMenu("menuId25", "Tom Yum Noodles Soup with Shrimp", "Spicy and sour Tom Yum noodle soup with shrimp", 14.00, "Soup & Noodles", "", Status.AVAILABLE),
        buildMenu("menuId26", "Tom Yum Noodles Soup with Seafood", "Spicy and sour Tom Yum noodle soup with seafood", 15.00, "Soup & Noodles", "", Status.AVAILABLE),
        buildMenu("menuId27", "Tom Yum Noodles Soup with Chicken", "Spicy and sour Tom Yum noodle soup with chicken", 14.00, "Soup & Noodles", "", Status.AVAILABLE),
        buildMenu("menuId28", "Cantonese Won Ton Noodles Soup", "Noodle soup with shrimp and pork wontons", 13.00, "Soup & Noodles", "", Status.AVAILABLE),
        buildMenu("menuId29", "Cantonese Style Beef Flank Noodles Soup", "Noodle soup with Cantonese-style beef flank", 14.00, "Soup & Noodles", "", Status.AVAILABLE),
        buildMenu("menuId30", "Chicken Ramen", "Japanese-style ramen with chicken", 14.00, "Soup & Noodles", "", Status.AVAILABLE),
        buildMenu("menuId31", "Beef Ramen", "Japanese-style ramen with beef", 15.00, "Soup & Noodles", "", Status.AVAILABLE),
        buildMenu("menuId32", "Shanghai Style Noodles Soup (Minced Pork)", "Shanghai-style noodle soup with minced pork", 13.00, "Soup & Noodles", "", Status.AVAILABLE),

        buildMenu("menuId33", "Chicken Teriyaki with Rice & Salad", "Grilled teriyaki chicken served with rice and salad", 14.00, "Asia Specialties", "", Status.AVAILABLE),
        buildMenu("menuId34", "Beef Teriyaki with Rice & Salad", "Grilled teriyaki beef served with rice and salad", 14.00, "Asia Specialties", "", Status.AVAILABLE),
        buildMenu("menuId35", "Chicken BBQ with Rice & Salad", "Vietnamese-style BBQ chicken served with rice and salad", 14.00, "Asia Specialties", "", Status.AVAILABLE),
        buildMenu("menuId36", "Beef BBQ with Rice & Salad", "Vietnamese-style BBQ beef served with rice and salad", 14.00, "Asia Specialties", "", Status.AVAILABLE),
        buildMenu("menuId37", "Tofu and Basil with Shrimps & Minced Pork", "Thai-style tofu with shredded basil, shrimp, and pork meat sauce", 15.00, "Asia Specialties", "", Status.AVAILABLE),
        buildMenu("menuId38", "Stir-Fried Chicken with Basil & Hot Peppers", "Thai-style spicy stir-fried chicken with basil and fresh hot peppers", 15.00, "Asia Specialties", "", Status.AVAILABLE),
        buildMenu("menuId39", "Sweet & Sour Fried Fish Fillet", "Crispy fried fish fillet in sweet & sour sauce", 15.00, "Asia Specialties", "", Status.AVAILABLE),
        buildMenu("menuId40", "Thai Style Yellow Curry Chicken", "Tender chicken cooked in Thai yellow curry", 15.00, "Asia Specialties", "", Status.AVAILABLE),
        buildMenu("menuId41", "Thai Style Yellow Curry Beef", "Tender beef cooked in Thai yellow curry", 15.00, "Asia Specialties", "", Status.AVAILABLE),
        buildMenu("menuId42", "Thai Style Yellow Curry Beef Flanks", "Slow-cooked beef flanks in Thai yellow curry", 15.00, "Asia Specialties", "", Status.AVAILABLE),
        buildMenu("menuId43", "Thai Style Yellow Curry Fish Fillet", "Fried fish fillet in Thai yellow curry sauce", 15.00, "Asia Specialties", "", Status.AVAILABLE),
        buildMenu("menuId44", "Beef Stir-Fried with Thai Satay", "Savory stir-fried beef in Thai satay sauce", 15.00, "Asia Specialties", "", Status.AVAILABLE),
        buildMenu("menuId45", "Chicken Stir-Fried with Thai Satay", "Savory stir-fried chicken in Thai satay sauce", 15.00, "Asia Specialties", "", Status.AVAILABLE),

        buildMenu("menuId46", "Seafood Hot Pot", "Hot pot with fish fillet, shrimp, squids, scallops, and tofu", 18.00, "Hot Pot or Sizzling Iron Plate", "", Status.AVAILABLE),
        buildMenu("menuId47", "Seafood Hot Pot with Vermicelli and Tofu", "Hot pot with seafood, vermicelli, and tofu", 18.00, "Hot Pot or Sizzling Iron Plate", "", Status.AVAILABLE),
        buildMenu("menuId48", "Seafood Hot Pot with Eggplants and Tofu", "Hot pot with seafood, eggplants, and tofu", 18.00, "Hot Pot or Sizzling Iron Plate", "", Status.AVAILABLE),
        buildMenu("menuId49", "Seafood Hot Pot with Spicy Basil", "Hot pot with seafood and tofu cooked with spicy basil", 18.00, "Hot Pot or Sizzling Iron Plate", "", Status.AVAILABLE),
        buildMenu("menuId50", "Beef Flank Hot Pot", "Hot pot with beef flank", 16.00, "Hot Pot or Sizzling Iron Plate", "", Status.AVAILABLE),
        buildMenu("menuId51", "Chicken, Tofu, and Mushrooms Hot Pot", "Hot pot with chicken, tofu, and mushrooms", 16.00, "Hot Pot or Sizzling Iron Plate", "", Status.AVAILABLE),
        buildMenu("menuId52", "Vegetables, Soft Tofu, and Eggplants Hot Pot", "Hot pot with vegetables, soft tofu, and eggplants", 16.00, "Hot Pot or Sizzling Iron Plate", "", Status.AVAILABLE),
        buildMenu("menuId53", "Sizzling Beef with Black Pepper", "Sizzling beef stir-fried with black pepper sauce", 19.00, "Hot Pot or Sizzling Iron Plate", "", Status.AVAILABLE),
        buildMenu("menuId54", "Sizzling Pork Chops with Black Pepper", "Sizzling pork chops stir-fried with black pepper sauce", 19.00, "Hot Pot or Sizzling Iron Plate", "", Status.AVAILABLE),
        buildMenu("menuId55", "Sizzling Seafood with Thai Sauce", "Sizzling seafood stir-fried with spicy Thai sauce", 19.00, "Hot Pot or Sizzling Iron Plate", "", Status.AVAILABLE),

        buildMenu("menuId56", "General Tao Chicken", "General Tao chicken", 14.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId57", "Sesame Chicken", "Sesame chicken", 14.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId58", "Crispy Chicken with Ginger Sauce", "Crispy chicken with ginger sauce", 14.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId59", "Salt and Pepper Chicken", "Salt and pepper chicken", 14.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId60", "Peanut Butter Chicken", "Peanut butter chicken", 15.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId61", "Five Spices Chicken", "Five spices chicken", 15.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId62", "Broccoli Stir Fried with Chicken", "Broccoli stir fried with chicken", 15.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId63", "Chicken with Hot Garlic Sauce", "Chicken with hot garlic sauce", 15.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId64", "Chicken Stir Fried with Wild Mushrooms", "Chicken stir fried with wild mushrooms", 15.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId65", "Chicken Stir Fried with Black Bean Sauce", "Chicken stir fried with black bean sauce", 15.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId66", "Chicken Stir Fried with Curry Sauce", "Chicken stir fried with curry sauce", 15.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId67", "Chicken Stir Fried with Ginger and Shallot", "Chicken stir fried with ginger and shallot", 15.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId68", "Chicken with Szechuanese Pepper Sauce", "Chicken with Szechuanese pepper sauce", 15.00, "Cantonese Style", "", Status.AVAILABLE),

        buildMenu("menuId69", "Orange Beef", "Orange beef", 15.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId70", "Sesame Beef", "Sesame beef", 15.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId71", "Broccoli Stir Fried with Beef", "Broccoli stir fried with beef", 15.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId72", "Beef with Hot Garlic Sauce", "Beef with hot garlic sauce", 15.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId73", "Beef Stir Fried with Wild Mushrooms", "Beef stir fried with wild mushrooms", 15.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId74", "Beef Stir Fried with Black Bean Sauce", "Beef stir fried with black bean sauce", 15.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId75", "Beef Stir Fried with Curry Sauce", "Beef stir fried with curry sauce", 15.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId76", "Beef Stir Fried with Ginger and Shallot", "Beef stir fried with ginger and shallot", 15.00, "Cantonese Style", "", Status.AVAILABLE),

        buildMenu("menuId77", "Garlic Shrimps (Fried)", "Garlic shrimps (Fried)", 16.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId78", "Salt and Pepper Shrimps (Fried)", "Salt and pepper shrimps (fried)", 16.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId79", "Salt and Pepper Squids (Fried)", "Salt and pepper squids (fried)", 16.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId80", "Salt and Pepper Shrimps and Squids (Fried)", "Salt and pepper shrimps and squids (fried)", 16.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId81", "Salt and Pepper Fish Fillet (Fried)", "Salt and pepper fish fillet (fried)", 16.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId82", "Stir Fried Chicken and Shrimps with Cashews", "Stir fried chicken and shrimps with cashews", 16.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId83", "Broccoli Stir Fried with Shrimps", "Broccoli stir fried with shrimps", 16.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId84", "Broccoli Stir Fried with Shrimps and Scallops", "Broccoli stir fried with shrimps and scallops", 16.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId85", "Broccoli Stir Fried with Fish Fillet", "Broccoli stir fried with fish fillet", 16.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId86", "Fish Fillet with Sweet Corn Sauce", "Fish fillet with sweet corn sauce", 16.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId87", "Fish Fillet Stir Fried with Ginger and Shallot", "Fish fillet stir fried with ginger and shallot", 16.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId88", "Szechuan Style Shrimps", "Szechuan style shrimps", 16.00, "Cantonese Style", "", Status.AVAILABLE),

        buildMenu("menuId89", "Spare Ribs", "Spare ribs", 17.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId90", "Deep Fried Pork", "Deep fried pork", 16.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId91", "Salt and Pepper Pork Chops", "Salt and pepper pork chops", 16.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId92", "Pork Chops Stir Fried with Ginger and Shallot", "Pork chops stir fried with ginger and shallot", 16.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId93", "Ma Po Tofu (Stir Fried with Minced Pork and Hot Sauce)", "Ma Po Tofu (stir fried with minced pork and hot sauce)", 13.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId94", "Yu Tsang Eggplants (Minced Pork with Hot and Sour Sauce)", "Yu Tsang eggplants (minced pork with hot and sour sauce)", 13.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId95", "Shrimps with Lobster Sauce (Minced Pork)", "Shrimps with lobster sauce (minced pork)", 15.00, "Cantonese Style", "", Status.AVAILABLE),

        buildMenu("menuId96", "General Tao Tofu (Fried)", "General Tao Tofu (fried)", 13.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId97", "Peanut Butter Tofu (Fried)", "Peanut butter tofu (fried)", 13.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId98", "Salt and Pepper Tofu (Fried)", "Salt and pepper tofu (fried)", 13.00, "Cantonese Style", "", Status.AVAILABLE),
        buildMenu("menuId99", "Vegetable and Tofu Curry", "Vegetable and tofu curry", 13.00, "Cantonese Style", "", Status.AVAILABLE),

        buildMenu("menuId106", "Cantonese Style Noodles (Shrimps, Chicken, Beef)", "Cantonese Style noodles (Shrimps, Chicken, Beef)", 15.00, "Cantonese Style Noodles or Rice", "", Status.AVAILABLE),
        buildMenu("menuId107", "Seafood and Vegetables", "Seafood and vegetables", 15.00, "Cantonese Style Noodles or Rice", "", Status.AVAILABLE),
        buildMenu("menuId108", "Shrimps and Vegetables", "Shrimps and vegetables", 15.00, "Cantonese Style Noodles or Rice", "", Status.AVAILABLE),
        buildMenu("menuId109", "Chicken or Beef and Vegetables", "Chicken or Beef and vegetables", 14.00, "Cantonese Style Noodles or Rice", "", Status.AVAILABLE),
        buildMenu("menuId110", "Chicken or Beef with Vegetables and Wild Mushrooms", "Chicken or Beef with vegetables and wild mushrooms", 14.00, "Cantonese Style Noodles or Rice", "", Status.AVAILABLE),
        buildMenu("menuId111", "Vegetables and Wild Mushrooms", "Vegetables and wild mushrooms", 14.00, "Cantonese Style Noodles or Rice", "", Status.AVAILABLE),
        buildMenu("menuId112", "Mixed Vegetables", "Mixed vegetables", 14.00, "Cantonese Style Noodles or Rice", "", Status.AVAILABLE),
        buildMenu("menuId113", "Chicken or Beef with Black Bean Sauce", "Chicken or Beef with black bean sauce", 14.00, "Cantonese Style Noodles or Rice", "", Status.AVAILABLE),
        buildMenu("menuId114", "Shrimps and Beef with Eggs Sauce", "Shrimps and Beef with eggs sauce", 14.00, "Cantonese Style Noodles or Rice", "", Status.AVAILABLE),
        buildMenu("menuId115", "Curry Chicken or Beef", "Curry Chicken or Beef", 14.00, "Cantonese Style Noodles or Rice", "", Status.AVAILABLE),
        buildMenu("menuId116", "Chicken or Beef and Oyster Sauce", "Chicken or Beef and oyster sauce", 14.00, "Cantonese Style Noodles or Rice", "", Status.AVAILABLE),
        buildMenu("menuId117", "Chicken and Tomato Sauce Stir Fried with Rice", "Chicken and tomato sauce stir fried with rice", 14.00, "Cantonese Style Noodles or Rice", "", Status.AVAILABLE),

        buildMenu("menuId118", "House Special Fried Rice (Shrimps, Chicken, Beef)", "House special fried rice (shrimp, chicken, beef)", 14.00, "Fried Rice", "", Status.AVAILABLE),
        buildMenu("menuId119", "Thai Style Shrimp Fried Rice (Spicy)", "Thai style shrimp fried rice (spicy)", 14.00, "Fried Rice", "", Status.AVAILABLE),
        buildMenu("menuId120", "Thai Style Chicken Fried Rice (Spicy)", "Thai style chicken fried rice (spicy)", 14.00, "Fried Rice", "", Status.AVAILABLE),
        buildMenu("menuId121", "Yang Chau Style Fried Rice (Shrimps, Ham)", "Yang Chau style fried rice (shrimp, ham)", 14.00, "Fried Rice", "", Status.AVAILABLE),
        buildMenu("menuId122", "Chicken Fried Rice", "Chicken fried rice", 13.00, "Fried Rice", "", Status.AVAILABLE),
        buildMenu("menuId123", "Beef Fried Rice", "Beef fried rice", 13.00, "Fried Rice", "", Status.AVAILABLE),
        buildMenu("menuId124", "Shrimps Fried Rice", "Shrimp fried rice", 14.00, "Fried Rice", "", Status.AVAILABLE),
        buildMenu("menuId125", "Seafood Fried Rice", "Seafood fried rice", 14.00, "Fried Rice", "", Status.AVAILABLE),
        buildMenu("menuId126", "Vegetables Fried Rice", "Vegetables fried rice", 13.00, "Fried Rice", "", Status.AVAILABLE),
        buildMenu("menuId127", "Egg Fried Rice", "Egg fried rice", 10.00, "Fried Rice", "", Status.AVAILABLE),

        buildMenu("menuId128", "Shrimps Pad Thai", "Shrimps Pad Thai", 14.00, "Asia Noodles", "", Status.AVAILABLE),
        buildMenu("menuId129", "Chicken Pad Thai", "Chicken Pad Thai", 14.00, "Asia Noodles", "", Status.AVAILABLE),
        buildMenu("menuId130", "Singapore Style Vermicelli", "Singapore Style Vermicelli", 14.00, "Asia Noodles", "", Status.AVAILABLE),
        buildMenu("menuId131", "Fujiyama Spaghetti with Chicken and Beef", "Fujiyama Spaghetti with chicken and beef", 14.00, "Asia Noodles", "", Status.AVAILABLE),
        buildMenu("menuId132", "Shanghai Style Noodles (Minced Pork)", "Shanghai Style Noodles (minced pork)", 14.00, "Asia Noodles", "", Status.AVAILABLE),
        buildMenu("menuId133", "Malaysian Rice Noodles with Chicken and Shrimps", "Malaysian rice noodles with chicken and shrimps", 14.00, "Asia Noodles", "", Status.AVAILABLE),
        buildMenu("menuId134", "Stir Fried Beef and Rice Noodles with Soy Sauce", "Stir fried beef and rice noodles with soy sauce", 14.00, "Asia Noodles", "", Status.AVAILABLE),
        buildMenu("menuId135", "Bean Sprouts Stir Fried with Noodles and Soy Sauce", "Bean sprouts stir fried with noodles and soy sauce", 12.00, "Asia Noodles", "", Status.AVAILABLE),

        buildMenu("menuId136", "Steam rice", "Steam rice", 3.50, "Extra", "", Status.AVAILABLE),
        buildMenu("menuId137", "Steam rice changed to fried rice", "Steam rice changed to fried rice", 4.50, "Extra", "", Status.AVAILABLE),
        buildMenu("menuId138", "Steam rice changed to noodles", "Steam rice changed to noodles", 4.50, "Extra", "", Status.AVAILABLE),
        buildMenu("menuId139", "Extra vegetables", "Extra vegetables", 4.00, "Extra", "", Status.AVAILABLE),
        buildMenu("menuId140", "Extra meat", "Extra meat", 5.00, "Extra", "", Status.AVAILABLE),
        buildMenu("menuId141", "Extra seafood", "Extra seafood", 6.00, "Extra", "", Status.AVAILABLE),
        buildMenu("menuId142", "Extra peanut butter sauce", "Extra peanut butter sauce", 4.00, "Extra", "", Status.AVAILABLE),
        buildMenu("menuId143", "Tea", "Tea", 2.00, "Drinks", "", Status.AVAILABLE),
        buildMenu("menuId144", "Soft drinks", "Soft drinks", 2.50, "Drinks", "", Status.AVAILABLE),
        buildMenu("menuId145", "Bottles of water", "Bottles of water", 2.00, "Drinks", "", Status.AVAILABLE)

        );

        Flux.fromIterable(menus)
                .flatMap(menu -> menuRepository.findMenuByMenuId(menu.getMenuId())
                        .switchIfEmpty(menuRepository.insert(menu))) // Only insert if not found
                .subscribe();
    }
    private void setupOrders() {
        Mono<Order> order1 = buildOrder("orderId1", "11111111-1111-1111-1111-111111111111", "Pending", LocalDate.now(),
                buildOrderDetails("menuId6", 2), buildOrderDetails("menuId22", 1));
        Mono<Order> order2 = buildOrder("orderId2", "22222222-2222-2222-2222-222222222222", "Completed", LocalDate.now().minusDays(1),
                buildOrderDetails("menuId88", 1), buildOrderDetails("menuId41", 3));
        Mono<Order> order3 = buildOrder("orderId3", "33333333-3333-3333-3333-333333333333", "Pending", LocalDate.now().minusDays(2),
                buildOrderDetails("menuId7", 1), buildOrderDetails("menuId63", 4));
        Mono<Order> order4 = buildOrder("orderId4", "44444444-4444-4444-4444-444444444444", "Canceled", LocalDate.now().minusDays(3),
                buildOrderDetails("menuId42", 2), buildOrderDetails("menuId83", 1));
        Mono<Order> order5 = buildOrder("orderId5", "55555555-5555-5555-5555-555555555555", "Pending", LocalDate.now(),
                buildOrderDetails("menuId22", 7), buildOrderDetails("menuId101", 3));
        Mono<Order> order6 = buildOrder("orderId6", "auth0|67853224d6b220fc6b4f86d9", "Pending", LocalDate.now(),
                buildOrderDetails("menuId10", 1), buildOrderDetails("menuId62", 1));
        Mono<Order> order7 = buildOrder("orderId7", "auth0|67853224d6b220fc6b4f86d9", "Pending", LocalDate.now(),
                buildOrderDetails("menuId8", 1), buildOrderDetails("menuId100", 1));

        Flux.just(order1, order2, order3, order4, order5, order6, order7)
                .flatMap(orderMono -> orderMono
                        .flatMap(order -> {
                            System.out.println("Checking if order exists: " + order.getOrderId());
                            return orderRepository.findByOrderId(order.getOrderId())
                                    .doOnTerminate(() -> System.out.println("Terminated: " + order.getOrderId()))
                                    .switchIfEmpty(Mono.defer(() -> {
                                        System.out.println("Inserting order: " + order.getOrderId());
                                        return orderRepository.insert(order);
                                    }));
                        }))
                .subscribe();
    }



    private Mono<Order> buildOrder(String orderId, String customerId, String status, LocalDate orderDate, Mono<OrderDetails>... orderDetails) {
        Flux<OrderDetails> orderDetailsFlux = Flux.just(orderDetails)
                .flatMap(mono -> mono);

        return orderDetailsFlux.collectList()
                .map(orderDetailsList -> {
                    double total = orderDetailsList.stream()
                            .mapToDouble(od -> od.getPrice() * od.getQuantity())
                            .sum();

                    return Order.builder()
                            .orderId(orderId)
                            .customerId(customerId)
                            .status(status)
                            .orderDate(orderDate)
                            .orderDetails(orderDetailsList)
                            .total(total)
                            .build();
                });
    }

    private Mono<OrderDetails> buildOrderDetails(String menuId, int quantity) {
        return menuRepository.findMenuByMenuId(menuId)
                .doOnNext(menuItem -> System.out.println("Fetched menu item: " + menuItem))
                .map(menuItem -> {
                    OrderDetails orderDetails = new OrderDetails();
                    orderDetails.setMenuId(menuId);
                    orderDetails.setQuantity(quantity);
                    orderDetails.setPrice(menuItem.getPrice());
                    System.out.println("Setting price in OrderDetails: " + menuItem.getPrice());
                    return orderDetails;
                });
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


    private void setupReviews() {
        Review review1 = buildReview("reviewId1", 5, "zako", "very good", "2022-11-24 13:00");
        Review review2 = buildReview("reviewId2", 3, "Christian", "very good", "2022-11-24 13:00");
        Review review3 = buildReview("reviewId3", 4, "Leopold", "very good", "2022-11-24 13:00");
        Review review4 = buildReview("reviewId4", 1, "Samuel", "very good", "2022-11-24 13:00");
        Review review5 = buildReview("reviewId5", 5, "Samantha", "very good", "2022-11-24 13:00");
        Review review6 = buildReview("reviewId6", 5, "zako", "very good", "2024-11-24 13:00");
        Review review7 = buildReview("reviewId7", 3, "Christian", "very good", "2023-11-24 13:00");
        Review review8 = buildReview("reviewId8", 4, "Leopold", "very good", "2025-11-24 13:00");
        Review review9 = buildReview("reviewId9", 1, "Samuel", "very good", "2023-11-24 13:00");
        Review review10 = buildReview("reviewId10", 5, "Samantha", "very good", "2024-11-24 13:00");

        Flux.just(review1, review2, review3, review4, review5, review6, review7, review8, review9, review10)
                .flatMap(review -> {
                    System.out.println("Checking if review exists: " + review.getReviewId());

                    // Ensure review does not already exist by reviewId and reviewerName
                    return reviewRepo.findReviewByIdAndReviewerName(review.getReviewId(), review.getReviewerName())
                            .doOnTerminate(() -> System.out.println("Terminated: " + review.getReviewId()))
                            .switchIfEmpty(Mono.defer(() -> {
                                System.out.println("Inserting review: " + review.getReviewId());
                                return reviewRepo.save(review); // Save if review doesn't exist
                            }));
                })
                .subscribe();
    }


    private Review buildReview(String reviewId, int rating, String reviewerName, String review, String dateSub) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime parsedReviewDate = LocalDateTime.parse(dateSub, formatter);
        return Review.builder()
                .reviewId(reviewId)
                .rating(rating)
                .reviewerName(reviewerName)
                .review(review)
                .dateSubmitted(parsedReviewDate)
                .isEdited(false)
                .build();
    }




    private void setupUsers() {
        User user1 = buildUser("auth0|675f6ad19a80612ce548e0a1", "zako@example.com", "Zako", "Smith", List.of("Customer"), null);
        User user2 = buildUser("google-oauth2|112850310681620335180", "christian@example.com", "Christian", "Johnson", List.of("Customer"), null);
        User user3 = buildUser("userId3", "leopold@example.com", "Leopold", "Miller", List.of("Customer"), null);
        User user4 = buildUser("userId4", "samuel@example.com", "Samuel", "Taylor", List.of("Staff"), null);
        User user5 = buildUser("userId5", "samantha@example.com", "Samantha", "Lee", List.of("Customer"), List.of("read"));
        User user6 = buildUser("auth0|67853224d6b220fc6b4f86d9", "felix@gmail.com", null, null, List.of("User", "Staff", "Owner", "Customer"), List.of("read:admin-messages", "read:current_user", "read:customer", "read:roles", "read:users", "write:role"));
        User user7 = buildUser("auth0|675f6ad19a80612ce548e0b2", "zako2@example.com", "Zama", "Smith", List.of("Customer"), null);
        User user8 = buildUser("auth0|675f6ad19a80612ce548e0c3", "zako3@example.com", "Zak", "Smith", List.of("Customer"), null);
        User user9 = buildUser("auth0|675f6ad19a80612ce548e0d4", "zako4@example.com", "Zala", "Smith", List.of("Customer"), null);
        User user10 = buildUser("auth0|675f6ad19a80612ce548e0e5", "zako5@example.com", "Zamel", "Smith", List.of("Customer"), null);
        User user11 = buildUser("auth0|675f6ad19a80612ce548e0i8", "zako6@example.com", "Zaba", "Smith", List.of("Customer"), null);


        Flux.just(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11)
                .flatMap(user -> {
                    System.out.println("Checking if user exists: " + user.getUserId());

                    // Check if the user already exists by userId (or email)
                    return userRepository.findByUserId(user.getUserId()) // Assuming userId is the unique identifier
                            .doOnTerminate(() -> System.out.println("Terminated: " + user.getUserId()))
                            .switchIfEmpty(Mono.defer(() -> {
                                System.out.println("Inserting user: " + user.getUserId());
                                return userRepository.save(user); // Save if user doesn't exist
                            }));
                })
                .subscribe();
    }


    private User buildUser(String userId, String email, String firstName, String lastName, List<String> roles, List<String> permissions) {
        return User.builder()
                .userId(userId)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .roles(roles)
                .permissions(permissions)
                .build();
    }




    private void setupReports() {
        Report report1 = buildReport("reportId1", "Most Popular Menu Item", "Burger", 150L, "2024-01-01 10:00");
        Report report2 = buildReport("reportId2", "Most Popular Menu Item", "Pizza", 120L, "2024-01-01 10:00");
        Report report3 = buildReport("reportId3", "Highest Revenue Menu Item", "Pasta", 200L, "2024-01-01 10:00");
        Report report4 = buildReport("reportId4", "Most Popular Menu Item", "Salad", 90L, "2024-01-01 10:00");
        Report report5 = buildReport("reportId5", "Highest Revenue Menu Item", "Steak", 250L, "2024-01-01 10:00");
        Report report6 = buildReport("reportId6", "Most Popular Menu Item", "Sushi", 130L, "2024-01-01 10:00");
        Report report7 = buildReport("reportId7", "Highest Revenue Menu Item", "Burger", 300L, "2024-01-01 10:00");
        Report report8 = buildReport("reportId8", "Most Popular Menu Item", "Tacos", 110L, "2024-01-01 10:00");
        Report report9 = buildReport("reportId9", "Highest Revenue Menu Item", "Pizza", 230L, "2024-01-01 10:00");
        Report report10 = buildReport("reportId10", "Most Popular Menu Item", "Pasta", 180L, "2024-01-01 10:00");

        Flux.just(report1, report2, report3, report4, report5, report6, report7, report8, report9, report10)
                .flatMap(report -> {
                    System.out.println("Checking if report exists: " + report.getReportId());

                    // Ensure report does not already exist by reportId
                    return reportRepo.findByReportId(report.getReportId())
                            .doOnTerminate(() -> System.out.println("Terminated: " + report.getReportId()))
                            .switchIfEmpty(Mono.defer(() -> {
                                System.out.println("Inserting report: " + report.getReportId());
                                return reportRepo.save(report); // Save if report doesn't exist
                            }));
                })
                .subscribe();
    }

    private Report buildReport(String reportId, String reportType, String menuItemName, Long itemCount, String generatedAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime parsedGeneratedAt = LocalDateTime.parse(generatedAt, formatter);
        return Report.builder()
                .reportId(reportId)
                .reportType(reportType)
                .menuItemName(menuItemName)
                .itemCount(itemCount)
                .generatedAt(parsedGeneratedAt)
                .build();
    }

    private void setupTeamMembers() {
        List<TeamMember> teamMembers = List.of(
                buildTeamMember("teamMemberId1", "John Doe", "Chef", "John is a professional chef with over 10 years of experience in the culinary arts.", "https://images.unsplash.com/photo-1654922207993-2952fec328ae?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
                buildTeamMember("teamMemberId2", "Jane Doe", "Sous Chef", "Jane is a sous chef with a passion for creating delicious and innovative dishes.", "https://images.unsplash.com/photo-1595257841889-eca2678454e2?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
                buildTeamMember("teamMemberId3", "Alice Smith", "Pastry Chef", "Alice is a talented pastry chef with a flair for creating beautiful and delicious desserts.", "https://images.unsplash.com/photo-1654922207993-2952fec328ae?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
                buildTeamMember("teamMemberId4", "Bob Johnson", "Line Cook", "Bob is a skilled line cook with a passion for creating high-quality dishes.", "https://cdn.pixabay.com/photo/2016/03/09/09/22/workplace-1245776_960_720.jpg"),
                buildTeamMember("teamMemberId5", "Sarah Williams", "Server", "Sarah is a friendly and attentive server with a passion for providing excellent customer service.", "https://cdn.pixabay.com/photo/2016/03/09/09/22/workplace-1245776_960_720.jpg")
        );

        teamMemberRepository.deleteAll() // Delete all existing records
                .thenMany(Flux.fromIterable(teamMembers))
                .flatMap(teamMemberRepository::insert) // Insert new records
                .subscribe();
    }

    private TeamMember buildTeamMember(String teamMemberId, String name, String role, String bio,String photoUrl) {
        TeamMember teamMember = new TeamMember();
        teamMember.setTeamMemberId(teamMemberId);
        teamMember.setName(name);
        teamMember.setRole(role);
        teamMember.setBio(bio);
        teamMember.setPhotoUrl(photoUrl);
        return teamMember;
    }

}
