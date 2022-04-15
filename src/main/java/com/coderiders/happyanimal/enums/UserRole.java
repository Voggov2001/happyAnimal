package com.coderiders.happyanimal.enums;

import com.coderiders.happyanimal.security.Permission;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum UserRole {
    ADMIN(Set.of(Permission.ADMIN)),
    EMPLOYEE(Set.of(Permission.EMPLOYEE)),
    VETERINARIAN(Set.of(Permission.VETERINARIAN)),
    SUPER_ADMIN(Set.of(Permission.ADMIN, Permission.EMPLOYEE, Permission.VETERINARIAN));

    private final Set<Permission> permissions;

    UserRole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
