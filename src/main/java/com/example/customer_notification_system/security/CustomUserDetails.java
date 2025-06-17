package com.example.customer_notification_system.security; // Place in security package

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    private Long id; // User ID
    private String role; // User's specific role

    public CustomUserDetails(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities, String role) {
        super(username, password, authorities);
        this.id = id;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    // You can override other methods if needed, but for now, this is enough
}
