package com.noodlestar.noodlestar.UserSubdomain.BusinessLayer;


import com.noodlestar.noodlestar.UserSubdomain.DataLayer.User;
import com.noodlestar.noodlestar.UserSubdomain.DataLayer.UserRepository;
import com.noodlestar.noodlestar.UserSubdomain.PresentationLayer.UserResponseModel;
import com.noodlestar.noodlestar.auth0.Auth0Service;
import com.noodlestar.noodlestar.utils.EntityDTOUtil;
import com.noodlestar.noodlestar.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Auth0Service auth0Service;
    private final UserRepository userRepository;

    @Override
    public Mono<UserResponseModel> addUserFromAuth0(String auth0UserId) {
        return auth0Service.getUserById(auth0UserId)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found with Auth0 ID: " + auth0UserId)))
                .flatMap(auth0User ->
                        userRepository.findByUserId(auth0UserId)
                                .switchIfEmpty(
                                        auth0Service.assignRoleToUser(auth0UserId, "rol_yNoNEH6304xc31YW")
                                                .doOnSuccess(unused -> log.info("Successfully assigned 'Customer' role to User ID: {}", auth0UserId))
                                                .doOnError(error -> log.error("Failed to assign 'Customer' role to User ID: {}", auth0UserId, error))
                                                .then(auth0Service.getUserById(auth0UserId)
                                                        .doOnSuccess(updatedAuth0User -> log.info("Updated Auth0 User Details After Role Assignment: {}", updatedAuth0User))
                                                        .flatMap(updatedAuth0User ->
                                                                userRepository.save(
                                                                        User.builder()
                                                                                .userId(auth0UserId)
                                                                                .email(updatedAuth0User.getEmail())
                                                                                .firstName(updatedAuth0User.getFirstName())
                                                                                .lastName(updatedAuth0User.getLastName())
                                                                                .roles(updatedAuth0User.getRoles())
                                                                                .permissions(updatedAuth0User.getPermissions())
                                                                                .build()
                                                                ).doOnSuccess(user -> log.info("User successfully created in MongoDB: {}", user))
                                                        )
                                                )
                                )
                )
                .map(EntityDTOUtil::toUserResponseModel)
                .doOnSuccess(user -> log.info("Final User Response: {}", user))
                .doOnError(error -> log.error("Error processing user with ID: {}", auth0UserId, error));
    }


    @Override
    public Mono<UserResponseModel> syncUserWithAuth0(String auth0UserId) {
        log.info("Starting user sync process for Auth0 User ID: {}", auth0UserId);

        return auth0Service.getUserById(auth0UserId)
                .doOnSuccess(auth0User -> log.info("Fetched Auth0 User Details: {}", auth0User))
                .flatMap(auth0User -> userRepository.findByUserId(auth0UserId)
                        .flatMap(existingUser -> {
                            log.info("Existing User Found in Database: {}", existingUser);

                            // Update User Fields
                            existingUser.setEmail(auth0User.getEmail());
                            existingUser.setFirstName(auth0User.getFirstName());
                            existingUser.setLastName(auth0User.getLastName());
                            existingUser.setRoles(auth0User.getRoles());
                            existingUser.setPermissions(auth0User.getPermissions());

                            log.info("Updated User Details Before Saving: {}", existingUser);

                            return userRepository.save(existingUser)
                                    .doOnSuccess(updatedUser -> log.info("Successfully Synced User in MongoDB: {}", updatedUser))
                                    .doOnError(error -> log.error("Failed to Save Synced User to MongoDB: {}", error.getMessage()));
                        })
                        .switchIfEmpty(Mono.error(new RuntimeException("User Not Found in Database: " + auth0UserId)))
                )
                .map(EntityDTOUtil::toUserResponseModel)
                .doOnSuccess(user -> log.info("Final Synced User Response: {}", user))
                .doOnError(error -> log.error("Error Syncing User with ID {}: {}", auth0UserId, error.getMessage()));
    }


    @Override
    public Mono<UserResponseModel> getUserByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found with ID: " + userId)))
                .map(EntityDTOUtil::toUserResponseModel)
                .doOnSuccess(user -> log.info("Fetched User from Database: {}", user))
                .doOnError(error -> log.error("Error fetching user with ID {}: {}", userId, error.getMessage()));
    }

    @Override
    public Flux<UserResponseModel> getStaff() {
        return userRepository.findAll()
                .filter(user -> user.getRoles() != null && user.getRoles().contains("Staff"))
                .map(EntityDTOUtil::toUserResponseModel)
                .doOnNext(user -> log.info("Fetched Staff from Database: {}", user))
                .doOnError(error -> log.error("Error fetching staff: {}", error.getMessage()));
    }

    public Mono<Void> deleteStaff(String userId) {
        return userRepository.findByUserId(userId)
                .filter(user -> user.getRoles() != null && user.getRoles().contains("Staff"))
                .switchIfEmpty(Mono.error(new NotFoundException("Staff member not found or not a valid staff")))
                .flatMap(user -> userRepository.delete(user))
                .doOnSuccess(unused -> log.info("Staff member with ID {} deleted successfully", userId))
                .doOnError(error -> log.error("Error deleting staff member: {}", error.getMessage()));
    }

    @Override
    public Mono<UserResponseModel> addStaffRoleToUser(String userId) {
        log.info("Starting process to add user with ID {} as staff", userId);

        return userRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found with ID: " + userId)))
                .flatMap(user -> {
                    if (user.getRoles() != null && user.getRoles().contains("Staff")) {
                        return Mono.error(new IllegalStateException("User with ID " + userId + " is already a staff member"));
                    }

                    // Update user roles
                    user.getRoles().add("Staff");

                    log.info("Assigning 'Staff' role to user with ID: {}", userId);

                    return auth0Service.assignRoleToUser(userId, "rol_1DSAOq7EC8sfW0KF")
                            .doOnSuccess(unused -> log.info("Successfully assigned 'Staff' role in Auth0 for user with ID: {}", userId))
                            .doOnError(error -> log.error("Failed to assign 'Staff' role in Auth0 for user with ID: {}", userId, error))
                            .then(userRepository.save(user))
                            .doOnSuccess(updatedUser -> log.info("User successfully updated as Staff in MongoDB: {}", updatedUser))
                            .doOnError(error -> log.error("Failed to save user with Staff role in MongoDB: {}", error.getMessage()));
                })
                .map(EntityDTOUtil::toUserResponseModel)
                .doOnSuccess(user -> log.info("Final Staff Member Response: {}", user))
                .doOnError(error -> log.error("Error adding staff member with ID {}: {}", userId, error.getMessage()));
    }

}

