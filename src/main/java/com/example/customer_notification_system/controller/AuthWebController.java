package com.example.customer_notification_system.controller;

import com.example.customer_notification_system.dto.requests.RegisterUserRequest;
import com.example.customer_notification_system.service.CustomerService;
import com.example.customer_notification_system.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

@Controller
@RequiredArgsConstructor
public class AuthWebController {

    private final CustomerService customerService;


    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid Username or Password!");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "auth/login"; // Refers to src/main/resources/templates/auth/login.html
    }



    /**
     * Handles the registration form submission.
     * @param request The DTO containing registration details.
     * @param redirectAttributes For flash messages on redirect.
     * @return Redirects to login on success, or back to register on error.
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerUserRequest") RegisterUserRequest request, RedirectAttributes redirectAttributes) {
        try {
            customerService.registerNewCustomer(request);
            redirectAttributes.addFlashAttribute("message", "Registration successful! Please log in.");
            return "redirect:/login";
        } catch (DuplicateResourceException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error during registration: " + e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/dashboard")
    public String redirectToDashboard(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) { // Ensure user is authenticated
            // Check if the user has SUPER_ADMIN or ADMIN role
            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN") || a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin/dashboard"; // Redirect to admin dashboard
            }
            // If authenticated but not an admin (and ROLE_USER is removed), redirect to access denied page
            return "redirect:/access-denied";
        }
        // Fallback to login if not authenticated
        return "redirect:/login";
    }


    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied"; // Refers to src/main/resources/templates/error/access-denied.html
    }


    @GetMapping("/")
    public String redirectToRoot() {
        return "redirect:/login"; // Redirect root URL to login page
    }
}