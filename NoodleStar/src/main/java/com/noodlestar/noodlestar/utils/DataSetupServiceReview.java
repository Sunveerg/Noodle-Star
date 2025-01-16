package com.noodlestar.noodlestar.utils;

import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Menu;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.MenuRepository;
import com.noodlestar.noodlestar.MenuSubdomain.DataLayer.Status;
import com.noodlestar.noodlestar.ReviewSubdomain.DataLayer.Review;
import com.noodlestar.noodlestar.ReviewSubdomain.DataLayer.ReviewRepo;
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


    @Override
    public void run(String... args) throws Exception {
        setupReviews();
        setupMenu();
        setupOrders();
        setupUsers();
    }



    private void setupMenu() {
        List<Menu> menus = List.of(
            buildMenu("menuId1", "Fried Rice", "Stir-frying rice with eggs and seafood.", 11.99, "Main Course", "https://cookingmadehealthy.com/wp-content/uploads/2021/03/Ratio-1-1-Baked-Chinese-Honey-Chicken-640x640.jpg", Status.AVAILABLE),
            buildMenu("menuId2", "Won Soup", "Dumplings filled with seasoned pork.", 5.99, "Appetizer", "https://png.pngtree.com/png-vector/20241120/ourlarge/pngtree-traditional-chinese-food-vector-illustration-png-image_14496318.png", Status.AVAILABLE),
            buildMenu("menuId3", "Fu Pot", "A seafood pot.", 15.99, "Main Course", "https://png.pngtree.com/png-vector/20241118/ourlarge/pngtree-chinese-food-on-white-background-png-image_14463713.png", Status.AVAILABLE),
            buildMenu("menuId4", "Pad Thai", "Thai Stir-fried noodle dish.", 15.99, "Main Course", "https://png.pngtree.com/png-vector/20240901/ourlarge/pngtree-chinese-food-kung-pao-chicken-in-transparent-background-png-image_13705724.png", Status.NOT_AVAILABLE),
            buildMenu("menuId5", "Kung Pao Chicken", "A spicy and savory Sichuan.", 12.99, "Main Course", "https://png.pngtree.com/png-vector/20241120/ourlarge/pngtree-chinese-food-vector-illustration-design-png-image_14496321.png", Status.NOT_AVAILABLE),

            buildMenu("menuId6", "Spring Rolls",
                    "A filling of vegetables.",
                    4.99, "Appetizer",
                    "https://lh3.googleusercontent.com/xX6_jY_mEcWapvchfiaDLLGrW6xpEB89M5KvyGMYg5pDAY8EqMygoQmQjBanf90pgouj000fIAcjxIBq7A3Jo_xJVp-64W442g=w640-h640-c-rw-v1-e365",
                    Status.AVAILABLE),

            buildMenu("menuId7", "Sichuan Fish",
                    "Fish cooked in a hot sauce.",
                    18.99, "Main Course",
                    "https://cookingmadehealthy.com/wp-content/uploads/2021/08/Baked-Sesame-Chicken-7-640x640.jpg",
                    Status.AVAILABLE),

            buildMenu("menuId8", "Beef and Broccoli",
                    "Chinese stir-fry dish.",
                    13.49, "Main Course",
                    "https://cookingmadehealthy.com/wp-content/uploads/2021/08/Healthy-Lemon-Chicken-Stir-Fry-10-640x640.jpg",
                    Status.AVAILABLE),

            buildMenu("menuId9", "Dumplings",
                    "Chinese dumplings with meat.",
                    7.99, "Appetizer",
                    "https://cdn.pixabay.com/photo/2024/02/21/06/48/ai-generated-8586854_640.png",
                    Status.AVAILABLE),

            buildMenu("menuId10", "Mongolian Beef",
                    "Stir-fried dish made with beef.",
                    14.49, "Main Course",
                    "https://www.chilihousesf.com/wp-content/uploads/2020/07/24091392.jpg",
                    Status.NOT_AVAILABLE)
        );

        Flux.fromIterable(menus)
                .flatMap(menu -> menuRepository.findMenuByMenuId(menu.getMenuId())
                        .switchIfEmpty(menuRepository.insert(menu))) // Only insert if not found
                .subscribe();
    }
    private void setupOrders() {
        Mono<Order> order1 = buildOrder("orderId1", "11111111-1111-1111-1111-111111111111", "Pending", LocalDate.now(),
                buildOrderDetails("menuId1", 2), buildOrderDetails("menuId2", 1));
        Mono<Order> order2 = buildOrder("orderId2", "22222222-2222-2222-2222-222222222222", "Completed", LocalDate.now().minusDays(1),
                buildOrderDetails("menuId3", 1), buildOrderDetails("menuId4", 3));
        Mono<Order> order3 = buildOrder("orderId3", "33333333-3333-3333-3333-333333333333", "Pending", LocalDate.now().minusDays(2),
                buildOrderDetails("menuId5", 1), buildOrderDetails("menuId6", 4));
        Mono<Order> order4 = buildOrder("orderId4", "44444444-4444-4444-4444-444444444444", "Canceled", LocalDate.now().minusDays(3),
                buildOrderDetails("menuId7", 2), buildOrderDetails("menuId8", 1));
        Mono<Order> order5 = buildOrder("orderId5", "55555555-5555-5555-5555-555555555555", "Pending", LocalDate.now(),
                buildOrderDetails("menuId9", 5), buildOrderDetails("menuId10", 3));

        Flux.just(order1, order2, order3, order4, order5)
                .flatMap(orderRepository::insert)
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
                .map(menuItem -> {
                    OrderDetails orderDetails = new OrderDetails();
                    orderDetails.setMenuId(menuId);
                    orderDetails.setQuantity(quantity);
                    orderDetails.setPrice(menuItem.getPrice());
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
        // Add more reviews...
        Flux.just(review1, review2, review3, review4, review5,review6,review7,review8,review9,review10)
                .flatMap(reviewRepo::insert)
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
                .build();
    }




    private void setupUsers() {
        User user1 = buildUser("auth0|675f6ad19a80612ce548e0a1", "zako@example.com", "Zako", "Smith", List.of("Customer"), null);
        User user2 = buildUser("google-oauth2|112850310681620335180", "christian@example.com", "Christian", "Johnson", List.of("Customer"), null);
        User user3 = buildUser("userId3", "leopold@example.com", "Leopold", "Miller", List.of("Customer"),null);
        User user4 = buildUser("userId4", "samuel@example.com", "Samuel", "Taylor", List.of("Staff"), null);
        User user5 = buildUser("userId5", "samantha@example.com", "Samantha", "Lee", List.of("Customer"), List.of("read"));
        // Add more users...

        Flux.just(user1, user2, user3, user4, user5)
                .flatMap(userRepository::insert)
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

}
