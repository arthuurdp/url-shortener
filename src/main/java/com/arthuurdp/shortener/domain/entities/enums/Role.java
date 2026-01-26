package com.arthuurdp.shortener.domain.entities.enums;

public enum Role {
    ROLE_ADMIN("admin"),
    ROLE_USER("user");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
