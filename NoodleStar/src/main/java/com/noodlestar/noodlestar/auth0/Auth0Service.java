package com.noodlestar.noodlestar.auth0;


import com.noodlestar.noodlestar.UserSubdomain.PresentationLayer.UserResponseModel;
import reactor.core.publisher.Mono;

public interface Auth0Service {

    Mono<UserResponseModel> getUserById(String auth0UserId);
    Mono<Void> assignRoleToUser(String auth0UserId, String roleName);
}
