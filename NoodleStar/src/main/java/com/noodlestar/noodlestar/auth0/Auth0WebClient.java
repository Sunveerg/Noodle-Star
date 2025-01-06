package com.noodlestar.noodlestar.auth0;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.noodlestar.noodlestar.UserSubdomain.PresentationLayer.UserResponseModel;
import com.noodlestar.noodlestar.auth0.models.*;
import com.noodlestar.noodlestar.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class Auth0WebClient implements Auth0Service {

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.clientSecret}")
    private String clientSecret;

    @Value("${auth0.clientId}")
    private String clientId;

    @Value("${auth0.audience}")
    private String audience;

    private final WebClient webClient;

    public Auth0WebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        // Replace with your Auth0 issuer URL
        String jwkSetUri = "https://" + domain + "/.well-known/jwks.json";

        log.info("Configuring JWT Decoder for URL: {}", jwkSetUri);

        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    private Mono<String> getAccessToken() {
        Auth0TokenRequestModel requestModel = new Auth0TokenRequestModel(
                clientId, clientSecret, audience, "client_credentials"
        );

        log.info("Requesting Auth0 Management API Access Token...");

        return webClient.post()
                .uri("https://" + domain + "/oauth/token")
                .header("Content-Type", "application/json")
                .bodyValue(requestModel)
                .retrieve()
                .bodyToMono(Auth0TokenResponseModel.class)
                .map(auth0TokenResponse -> {

                    String accessToken = auth0TokenResponse.getAccessToken();
                    log.info("Auth0 Access Token Retrieved Successfully.");

                    // Decode the JWT token and extract permissions
                    DecodedJWT decodedJWT = JWT.decode(accessToken);

                  /*  // Safely extract the "permissions" claim (if it exists)
                    List<String> permissions = decodedJWT.getClaims()
                            .get("permissions") // Get the "permissions" claim
                            .asList(String.class); // This can return null if the claim doesn't exist

                    // Log permissions only if they are present
                    if (permissions != null && !permissions.isEmpty()) {
                        log.info("Permissions from the token: {}", permissions);
                    } else {
                        log.warn("No permissions found in the token.");
                    }*/

                    return accessToken; // Return the access token for further use
                })
                .doOnError(error -> log.error("Failed to retrieve Auth0 Access Token: {}", error.getMessage(), error));
    }



    @Override
    public Mono<UserResponseModel> getUserById(String auth0UserId) {
        log.info("Fetching Auth0 User Details for User ID: {}", auth0UserId);

        return getAccessToken()
                .flatMap(token -> {
                    Mono<Auth0UserResponseModel> userMono = fetchUser(auth0UserId, token);
                    Flux<Auth0RoleResponseModel> rolesFlux = fetchRoles(auth0UserId, token);
                    Flux<Auth0PermissionResponseModel> permissionsFlux = fetchPermissions(auth0UserId, token);

                    return Mono.zip(userMono, rolesFlux.collectList(), permissionsFlux.collectList())
                            .map(tuple -> mapToUserResponseModel(auth0UserId, tuple.getT1(), tuple.getT2(), tuple.getT3()));
                })
                .doOnSuccess(user -> log.info("Successfully Retrieved User Response: {}", user))
                .doOnError(error -> {
                    if (error instanceof NotFoundException) {
                        log.warn("User Not Found: {}", error.getMessage());
                    } else {
                        log.error("Failed to retrieve user by ID: {}", auth0UserId, error);
                    }
                });
    }

    private Mono<Auth0UserResponseModel> fetchUser(String auth0UserId, String token) {
        return webClient.get()
                .uri("https://" + domain + "/api/v2/users/" + auth0UserId)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(Auth0UserResponseModel.class)
                .doOnSuccess(user -> log.info("Fetched Auth0 User with ID {}: {}", auth0UserId, user))
                .doOnError(WebClientResponseException.class, error -> {
                    if (error.getStatusCode().is4xxClientError()) {
                        log.warn("User Not Found in Auth0: {}", auth0UserId);
                    } else {
                        log.error("Failed to fetch Auth0 User with ID: {}", auth0UserId, error);
                    }
                });
    }

    private Flux<Auth0RoleResponseModel> fetchRoles(String auth0UserId, String token) {
        return webClient.get()
                .uri("https://" + domain + "/api/v2/users/" + auth0UserId + "/roles")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToFlux(Auth0RoleResponseModel.class)
                .doOnNext(role -> log.info("Fetched Role for User ID {}: {}", auth0UserId, role))
                .doOnError(error -> log.error("Failed to fetch roles for User ID: {}", auth0UserId, error));
    }

    private Flux<Auth0PermissionResponseModel> fetchPermissions(String auth0UserId, String token) {
        return webClient.get()
                .uri("https://" + domain + "/api/v2/users/" + auth0UserId + "/permissions")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToFlux(Auth0PermissionResponseModel.class)
                .doOnNext(permission -> log.info("Fetched Permission for User ID {}: {}", auth0UserId, permission))
                .doOnError(error -> log.error("Failed to fetch permissions for User ID: {}", auth0UserId, error));
    }

    private UserResponseModel mapToUserResponseModel(
            String auth0UserId,
            Auth0UserResponseModel auth0User,
            List<Auth0RoleResponseModel> roles,
            List<Auth0PermissionResponseModel> permissions) {

        List<String> roleNames = roles.stream().map(Auth0RoleResponseModel::getName).toList();
        List<String> permissionNames = permissions.stream().map(Auth0PermissionResponseModel::getName).toList();

        log.info("Mapping User Response for User ID {} with Roles and Permissions", auth0UserId);

        return UserResponseModel.builder()
                .userId(auth0User.getUserId())
                .email(auth0User.getEmail())
                .firstName(auth0User.getFirstName())
                .lastName(auth0User.getLastName())
                .roles(roleNames)
                .permissions(permissionNames)
                .build();
    }

    @Override
    public Mono<Void> assignRoleToUser(String auth0UserId, String roleName) {
        log.info("Assigning Role '{}' to User ID: {}", roleName, auth0UserId);

        return getAccessToken()
                .flatMap(token -> webClient.post()
                        .uri("https://" + domain + "/api/v2/users/" + auth0UserId + "/roles")
                        .headers(headers -> headers.setBearerAuth(token))
                        .bodyValue(new AssignRolesRequestModel(roleName))
                        .retrieve()
                        .toBodilessEntity()
                        .then())
                .doOnSuccess(aVoid -> log.info("Role '{}' assigned successfully to User ID: {}", roleName, auth0UserId))
                .doOnError(error -> log.error("Failed to assign role '{}' to User ID: {}", roleName, auth0UserId, error));
    }
}
