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
public class CustomerWebController { // Renamed to avoid conflict with REST CustomerController

    private final CustomerService customerService;

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

    // TODO: Add methods for /customers/edit/{id} and /customers/delete/{id} as POST for form submission
    // For delete, you'd generally use a POST redirect GET pattern to avoid re-submitting on refresh.
        /*
        @PostMapping("/customers/delete/{id}")
        public String deleteCustomer(@PathVariable Long id) {
            customerService.deleteCustomer(id);
            return "redirect:/customers";
        }
        */
}
    