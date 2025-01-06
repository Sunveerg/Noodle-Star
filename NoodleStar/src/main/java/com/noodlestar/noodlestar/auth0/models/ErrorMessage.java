package com.noodlestar.noodlestar.auth0.models;

public record ErrorMessage(String message) {

    public static ErrorMessage from(final String message) {
        return new ErrorMessage(message);
    }
}