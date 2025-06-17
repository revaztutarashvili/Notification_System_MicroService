package com.example.customer_notification_system.controller;

import com.example.customer_notification_system.dto.CustomerDTO;
import com.example.customer_notification_system.dto.requests.CreateCustomerRequest;
import com.example.customer_notification_system.dto.requests.UpdateCustomerRequest;
import com.example.customer_notification_system.exception.ResourceNotFoundException;
import com.example.customer_notification_system.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication; // Import Authentication

import java.util.List;

@Controller // Use @Controller for rendering views (not @RestController)
@RequestMapping("/") // Base path for web pages - Specific sub-paths will be handled
@RequiredArgsConstructor
public class CustomerWebController {

    private final CustomerService customerService;

    // The root path '/' is now handled by AuthWebController which redirects to /login.
    // The registration endpoint is also handled by AuthWebController.
    // Removed: @GetMapping("/register") and @PostMapping("/register") to resolve ambiguous mapping.

    /**
     * Displays a list of all customers.
     * Accessible by admins (ROLE_SUPER_ADMIN, ROLE_ADMIN).
     * @param model The model to add the list of customers.
     * @return The name of the customers list Thymeleaf template.
     */
    @GetMapping("/admin/customers") // Now under /admin/ path
    // Spring Security will already handle authorization for /admin/** paths via SecurityConfig.
    // No @PreAuthorize needed here as the path itself is secured.
    public String listCustomers(Model model, Authentication authentication) {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        // You might want to add current user's role/username to the model for display
        // if (authentication != null) {
        //     model.addAttribute("currentUsername", authentication.getName());
        //     model.addAttribute("currentUserRoles", authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.joining(", ")));
        // }
        return "admin/customers"; // Refers to src/main/resources/templates/admin/customers.html
    }

    /**
     * Displays the form to edit an existing customer.
     * Accessible by admins.
     * Fetches the customer data and pre-populates the form.
     * @param id The ID of the customer to edit.
     * @param model The model to add the customer data.
     * @param redirectAttributes For flash messages on redirect.
     * @return The name of the customer edit Thymeleaf template, or redirects on error.
     */
    @GetMapping("/admin/customers/edit/{id}") // Now under /admin/ path
    public String showEditCustomerForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            CustomerDTO customer = customerService.getCustomerById(id);
            UpdateCustomerRequest updateRequest = new UpdateCustomerRequest();
            updateRequest.setFullName(customer.getFullName());
            updateRequest.setEmail(customer.getEmail());
            updateRequest.setPhoneNumber(customer.getPhoneNumber());

            model.addAttribute("customerId", id);
            model.addAttribute("updateCustomerRequest", updateRequest);
            return "admin/customer-edit"; // Refers to src/main/resources/templates/admin/customer-edit.html
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/customers";
        }
    }

    /**
     * Handles the form submission for updating an existing customer.
     * Accessible by admins.
     * @param id The ID of the customer to update.
     * @param request The DTO containing updated customer details from the form.
     * @param redirectAttributes For flash messages on redirect.
     * @return Redirects to the customers list page on success or error.
     */
    @PostMapping("/admin/customers/update/{id}") // Now under /admin/ path
    public String updateCustomer(@PathVariable Long id, @ModelAttribute("updateCustomerRequest") UpdateCustomerRequest request, RedirectAttributes redirectAttributes) {
        try {
            customerService.updateCustomer(id, request);
            redirectAttributes.addFlashAttribute("message", "Customer updated successfully!");
        } catch (ResourceNotFoundException | com.example.customer_notification_system.exception.DuplicateResourceException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/customers";
    }

    /**
     * Handles the form submission for deleting a customer.
     * Accessible by admins.
     * Using POST for deletion from a form is a common practice to avoid GET requests with side effects.
     * @param id The ID of the customer to delete.
     * @param redirectAttributes For flash messages on redirect.
     * @return Redirects to the customers list page.
     */
    @PostMapping("/admin/customers/delete/{id}") // Now under /admin/ path
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("message", "Customer deleted successfully!");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/customers";
    }

    /**
     * Displays the form to create a new customer.
     * Accessible by admins.
     * @param model The model to add attributes for the view.
     * @return The name of the customer creation Thymeleaf template.
     */
    @GetMapping("/admin/customers/new") // New endpoint for creating customers via admin UI
    public String showCreateCustomerForm(Model model) {
        model.addAttribute("createCustomerRequest", new CreateCustomerRequest());
        return "admin/customer-create"; // Refers to src/main/resources/templates/admin/customer-create.html
    }

    /**
     * Handles the form submission for creating a new customer.
     * Accessible by admins.
     * @param request The DTO containing details for the new customer.
     * @param redirectAttributes For flash messages on redirect.
     * @return Redirects to the customers list page on success or error.
     */
    @PostMapping("/admin/customers/create") // New endpoint for creating customers via admin UI
    public String createCustomer(@ModelAttribute("createCustomerRequest") CreateCustomerRequest request, RedirectAttributes redirectAttributes) {
        try {
            customerService.createCustomer(request);
            redirectAttributes.addFlashAttribute("message", "Customer created successfully!");
        } catch (com.example.customer_notification_system.exception.DuplicateResourceException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating customer: " + e.getMessage());
        }
        return "redirect:/admin/customers";
    }
}
