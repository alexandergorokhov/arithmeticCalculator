package org.challenge.controller;

import lombok.Getter;

@Getter
public final class LoginRequest {
    String username;
    String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
