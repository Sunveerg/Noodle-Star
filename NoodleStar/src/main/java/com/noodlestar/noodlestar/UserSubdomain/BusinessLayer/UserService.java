package com.noodlestar.noodlestar.UserSubdomain.BusinessLayer;


import com.noodlestar.noodlestar.UserSubdomain.PresentationLayer.UserResponseModel;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<UserResponseModel> addUserFromAuth0(String auth0UserId);
    Mono<UserResponseModel> syncUserWithAuth0(String auth0UserId);
    Mono<UserResponseModel> getUserByUserId(String auth0UserId);
}

