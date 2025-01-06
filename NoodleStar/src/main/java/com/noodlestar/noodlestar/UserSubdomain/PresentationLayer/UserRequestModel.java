package com.noodlestar.noodlestar.UserSubdomain.PresentationLayer;

import lombok.Data;

import java.util.List;

@Data
public class UserRequestModel {
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
    private List<String> permissions;
}
