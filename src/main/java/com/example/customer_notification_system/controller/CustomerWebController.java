package com.example.customer_notification_system.controller;

import com.example.customer_notification_system.dto.CustomerDTO;
import com.example.customer_notification_system.dto.requests.RegisterUserRequest;
import com.example.customer_notification_system.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller // Use @Controller for rendering views (not @RestController)
@RequestMapping("/") // Base path for web pages
@RequiredArgsConstructor
public class CustomerWebController {

    private final CustomerService customerService;

    /**
     * Redirects the root URL to the customers list page.
     * This provides a default landing page for the application.
     */
    @GetMapping("/") // Handle requests to the root URL (http://localhost:8082/)
    public String redirectToCustomersList() {
        return "redirect:/customers"; // Redirect to the /customers endpoint
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerUserRequest", new RegisterUserRequest());
        return "register"; // Refers to src/main/resources/templates/register.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerUserRequest") RegisterUserRequest request, Model model) {
        try {
            customerService.registerUser(request);
            model.addAttribute("message", "Registration successful!");
            return "redirect:/customers"; // Redirect to customer list or login
        } catch (IllegalArgumentException e) { // Catch specific exceptions from service
            model.addAttribute("error", e.getMessage());
            return "register"; // Stay on registration page with error
        }
    }

    @GetMapping("/customers")
    public String listCustomers(Model model) {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customers"; // Refers to src/main/resources/templates/customers.html
    }

    // ... (other methods like /customers/edit/{id} and /customers/delete/{id})
}
