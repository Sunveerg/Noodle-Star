package com.noodlestar.noodlestar.auth0.models;

import lombok.Getter;

@Getter
public class AccessTokenRequest {
    private String clientId;
    private String clientSecret;
    private String audience;
    private String grantType;

    public AccessTokenRequest(String clientId, String clientSecret, String audience, String clientCredentials) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.audience = audience;
        this.grantType = clientCredentials;
    }

    @Override
    public String toString() {
        return "{\"client_id\":\"" + clientId + "\",\"client_secret\":\"" + clientSecret + "\",\"audience\":\"" + audience + "\",\"grant_type\":\"" + grantType + "\"}";
    }
}
