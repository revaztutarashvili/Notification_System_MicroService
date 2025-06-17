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
import org.springframework.security.core.Authentication; // Correct import for Spring Security's Authentication

@Controller
@RequiredArgsConstructor
public class AuthWebController {

    private final CustomerService customerService;

    /**
     * Displays the login page.
     * @param error Model attribute to indicate login error.
     * @param logout Model attribute to indicate successful logout.
     * @param model The model to add attributes for the view.
     * @return The name of the login Thymeleaf template.
     */
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("loginError", "Invalid username or password.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out successfully.");
        }
        return "auth/login"; // Refers to src/main/resources/templates/auth/login.html
    }

    /**
     * Displays the registration form.
     * @param model The model to add attributes for the view.
     * @return The name of the registration Thymeleaf template.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerUserRequest", new RegisterUserRequest());
        return "auth/register"; // Refers to src/main/resources/templates/auth/register.html
    }

    /**
     * Handles the user registration form submission.
     * Redirects to login page on success or stays on registration page with error.
     * @param request The DTO containing registration details from the form.
     * @param redirectAttributes For flash messages on redirect.
     * @param model The model to add attributes (e.g., error messages).
     * @return Redirects to login page on success, or stays on register page with error.
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerUserRequest") RegisterUserRequest request,
                               RedirectAttributes redirectAttributes, Model model) {
        try {
            customerService.registerUser(request);
            redirectAttributes.addFlashAttribute("message", "Registration successful! Please log in.");
            return "redirect:/login"; // Redirect to login page after successful registration
        } catch (DuplicateResourceException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register"; // Stay on registration page with error
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred during registration: " + e.getMessage());
            return "auth/register";
        }
    }

    /**
     * Redirects to the appropriate dashboard after successful login.
     * This method intercepts the /dashboard path and redirects based on the authenticated user's role.
     * @param authentication The Spring Security Authentication object containing user details and authorities.
     * @return A redirection string to the appropriate dashboard URL.
     */
    @GetMapping("/dashboard")
    public String redirectToDashboard(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) { // Ensure user is authenticated
            // Check if the user has SUPER_ADMIN or ADMIN role
            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN") || a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin/dashboard"; // Redirect to admin dashboard
            }
            // Check if the user has USER role
            else if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
                return "redirect:/user/dashboard"; // Redirect to user dashboard
            }
        }
        return "redirect:/login"; // Fallback to login if not authenticated or no matching role
    }

    /**
     * Default root path redirect to login.
     * @return Redirects to the login page.
     */
    @GetMapping("/")
    public String redirectToRoot() {
        return "redirect:/login"; // Redirect root URL to login page
    }
}
