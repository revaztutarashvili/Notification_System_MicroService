package com.example.customer_notification_system.controller;
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.*;
import com.example.customer_notification_system.service.*;
import com.example.customer_notification_system.security.CustomUserDetails; // Import CustomUserDetails
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import @PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Import @AuthenticationPrincipal
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    /**
     * Creates a new administrator account.
     * Only SUPER_ADMINs and ADMINs can create new admins.
     * ADMINs cannot create SUPER_ADMINs.
     *
     * @param username The username for the new admin.
     * @param password The password for the new admin.
     * @param role The role for the new admin (e.g., "ROLE_ADMIN"). Only SUPER_ADMIN can set "ROLE_SUPER_ADMIN".
     * @param currentUser The authenticated user.
     * @return ResponseEntity with the created AdminDTO and HTTP status 200 OK.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<AdminDTO> createAdmin(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(defaultValue = "ROLE_ADMIN") String role, // Default to ADMIN
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        // Logic to prevent ADMIN from creating SUPER_ADMIN
        if (role.equals("ROLE_SUPER_ADMIN") && !currentUser.getRole().equals("ROLE_SUPER_ADMIN")) {
            return ResponseEntity.status(403).build(); // Forbidden
        }
        // Ensure role is a valid admin role
        if (!role.equals("ROLE_ADMIN") && !role.equals("ROLE_SUPER_ADMIN")) {
            return ResponseEntity.badRequest().build(); // Invalid role
        }

        return ResponseEntity.ok(adminService.createAdmin(username, password, role));
    }

    /**
     * Retrieves an administrator's details by their username.
     * Only SUPER_ADMINs and ADMINs can view other admins.
     *
     * @param username The username of the admin to retrieve.
     * @return ResponseEntity with the AdminDTO and HTTP status 200 OK.
     */
    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<AdminDTO> getAdmin(@PathVariable String username) {
        return ResponseEntity.ok(adminService.getAdminByUsername(username));
    }

    // TODO: Add deleteAdmin endpoint and implement logic as specified (SuperAdmin cannot delete SuperAdmin)
    // Example delete:
    /*
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN') and (@adminService.isAdminDeleteAllowed(#id, authentication.principal.id, authentication.principal.authorities))")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails currentUser) {
        adminService.deleteAdmin(id); // This method needs to exist and verify roles internally
        return ResponseEntity.noContent().build();
    }
    */
    // TODO: Add updateAdmin endpoint (only SUPER_ADMIN can change roles, and no one can update superadmin's role to non-superadmin)
}
