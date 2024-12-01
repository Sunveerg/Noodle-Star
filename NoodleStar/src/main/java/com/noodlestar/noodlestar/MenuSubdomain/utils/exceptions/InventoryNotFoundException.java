package com.noodlestar.noodlestar.MenuSubdomain.utils.exceptions;

public class InventoryNotFoundException extends RuntimeException {

    public InventoryNotFoundException(String message) {
        super(message);
    }

}