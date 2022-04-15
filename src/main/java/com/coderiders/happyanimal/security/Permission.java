package com.coderiders.happyanimal.security;

public enum Permission {
    ADMIN("admin"),
    EMPLOYEE("employee"),
    VETERINARIAN("veterinarian");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
