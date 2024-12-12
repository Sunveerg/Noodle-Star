package com.noodlestar.noodlestar.utils.exceptions;

public class MenuItemDoesNotExistException extends RuntimeException {
    public MenuItemDoesNotExistException(String message) {
        super(message);
    }
}