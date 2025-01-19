package com.noodlestar.noodlestar.UserSubdomain.PresentationLayer;


import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuRequestModel;
import com.noodlestar.noodlestar.MenuSubdomain.PresentationLayer.MenuResponseModel;
import com.noodlestar.noodlestar.UserSubdomain.BusinessLayer.UserService;
import com.noodlestar.noodlestar.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    private final UserService userService;

    @PostMapping("/{userId}/login")
    public Mono<ResponseEntity<UserResponseModel>> handleUserLogin(@PathVariable String userId) {
        return userService.addUserFromAuth0(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/sync")
    public Mono<ResponseEntity<UserResponseModel>> syncUser(@PathVariable String userId) {
        return userService.syncUserWithAuth0(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<UserResponseModel> getAllUsers() {
        log.info("Received request to fetch all users.");
        return userService.getAllUsers()
                .doOnNext(user -> log.info("Fetched user: {}", user))
                .doOnError(e -> log.error("Error fetching all users: {}", e.getMessage()));
    }

    @GetMapping("/{userId}")
    public Mono<UserResponseModel> getUserByUserId(@PathVariable String userId) {
        return userService.getUserByUserId(userId);
    }

    @GetMapping("/staff")
    public Flux<UserResponseModel> getStaff() {
        return userService.getStaff();
    }

    @DeleteMapping("/staff/{userId}")
    public Mono<ResponseEntity<Void>> deleteStaff(@PathVariable String userId) {
        return userService.deleteStaff(userId)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .onErrorResume(NotFoundException.class, e -> Mono.just(new ResponseEntity<Void>(HttpStatus.NOT_FOUND)));
    }

    @PutMapping("/staff/{userId}")
    public Mono<ResponseEntity<UserResponseModel>> updateStaff(@RequestBody Mono<UserRequestModel> userRequestModel, @PathVariable String userId) {
        return userService.updateStaff(userRequestModel, userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/staff/{userId}")
    public Mono<ResponseEntity<UserResponseModel>> addStaffMember(@PathVariable String userId) {
        log.info("Received request to add user with ID {} as staff", userId);

        return userService.addStaffRoleToUser(userId)
                .map(ResponseEntity::ok)
                .onErrorResume(NotFoundException.class, e -> {
                    log.error("User not found: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                })
                .onErrorResume(IllegalStateException.class, e -> {
                    log.error("Invalid operation: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build());
                })
                .doOnError(e -> log.error("Error processing addStaffMember request: {}", e.getMessage()));
    }

}
