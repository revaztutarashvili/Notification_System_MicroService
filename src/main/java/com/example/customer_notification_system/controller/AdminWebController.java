package com.example.customer_notification_system.controller;

import com.example.customer_notification_system.dto.AdminDTO;
import com.example.customer_notification_system.dto.CustomerDTO;
import com.example.customer_notification_system.dto.requests.CreateAdminRequest;
import com.example.customer_notification_system.dto.requests.CreateCustomerRequest;
import com.example.customer_notification_system.dto.requests.UpdateAdminRequest;
import com.example.customer_notification_system.dto.requests.UpdateCustomerRequest;
import com.example.customer_notification_system.exception.ResourceNotFoundException;
import com.example.customer_notification_system.service.AdminService;
import com.example.customer_notification_system.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin") // Base path for all admin web pages
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')") // All methods in this controller require ADMIN or SUPER_ADMIN role
public class AdminWebController {

    private final CustomerService customerService;
    private final AdminService adminService;

    // --- Admin Dashboard ---
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // You can add data to the model for the dashboard if needed
        return "admin/dashboard"; // Renders src/main/resources/templates/admin/dashboard.html
    }

    // --- Customer Management ---

    @GetMapping("/customers")
    public String listCustomers(Model model) {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "admin/customers"; // Renders src/main/resources/templates/admin/customers.html
    }

    @GetMapping("/customers/new")
    public String createCustomerForm(Model model) {
        model.addAttribute("createCustomerRequest", new CreateCustomerRequest());
        return "admin/customer-create"; // Renders src/main/resources/templates/admin/customer-create.html
    }

    @PostMapping("/customers/create")
    public String createCustomer(@Valid @ModelAttribute("createCustomerRequest") CreateCustomerRequest request,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/customer-create";
        }
        try {
            customerService.createCustomer(request);
            redirectAttributes.addFlashAttribute("message", "Customer created successfully!");
            return "redirect:/admin/customers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating customer: " + e.getMessage());
            return "redirect:/admin/customers/new";
        }
    }

    @GetMapping("/customers/edit/{id}")
    public String editCustomerForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            CustomerDTO customer = customerService.getCustomerById(id);
            UpdateCustomerRequest updateRequest = new UpdateCustomerRequest();
            updateRequest.setFullName(customer.getFullName());
            updateRequest.setEmail(customer.getEmail());
            updateRequest.setPhoneNumber(customer.getPhoneNumber());
            // Note: Username cannot be updated via this DTO as per our current setup.
            model.addAttribute("customerId", id);
            model.addAttribute("updateCustomerRequest", updateRequest);
            return "admin/customer-edit"; // Renders src/main/resources/templates/admin/customer-edit.html
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/customers";
        }
    }

    @PostMapping("/customers/update/{id}")
    public String updateCustomer(@PathVariable Long id,
                                 @Valid @ModelAttribute("updateCustomerRequest") UpdateCustomerRequest request,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Need to re-add customerId for the form to correctly post back
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.updateCustomerRequest", result);
            redirectAttributes.addFlashAttribute("updateCustomerRequest", request);
            return "redirect:/admin/customers/edit/" + id;
        }
        try {
            customerService.updateCustomer(id, request);
            redirectAttributes.addFlashAttribute("message", "Customer updated successfully!");
            return "redirect:/admin/customers";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/customers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating customer: " + e.getMessage());
            return "redirect:/admin/customers/edit/" + id;
        }
    }

    @PostMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("message", "Customer deleted successfully!");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting customer: " + e.getMessage());
        }
        return "redirect:/admin/customers";
    }

    // --- Admin Management ---

    @GetMapping("/admins")
    public String listAdmins(Model model) {
        List<AdminDTO> admins = adminService.getAllAdmins(); // Assuming AdminService has getAllAdmins()
        model.addAttribute("admins", admins);
        return "admin/admins"; // Renders src/main/resources/templates/admin/admins.html
    }

    @GetMapping("/admins/new")
    public String createAdminForm(Model model) {
        model.addAttribute("createAdminRequest", new CreateAdminRequest());
        return "admin/admin-create"; // Renders src/main/resources/templates/admin/admin-create.html
    }

    @PostMapping("/admins/create")
    public String createAdmin(@Valid @ModelAttribute("createAdminRequest") CreateAdminRequest request,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/admin-create";
        }
        try {
            adminService.createAdmin(request.getUsername(), request.getPassword(), request.getRole());
            redirectAttributes.addFlashAttribute("message", "Admin created successfully!");
            return "redirect:/admin/admins";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating admin: " + e.getMessage());
            return "redirect:/admin/admins/new";
        }
    }

    @GetMapping("/admins/edit/{id}")
    public String editAdminForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            AdminDTO admin = adminService.getAdminById(id); // Assuming AdminService has getAdminById()
            UpdateAdminRequest updateRequest = new UpdateAdminRequest();
            updateRequest.setUsername(admin.getUsername());
            updateRequest.setRole(admin.getRole());
            model.addAttribute("adminId", id);
            model.addAttribute("updateAdminRequest", updateRequest);
            return "admin/admin-edit"; // Renders src/main/resources/templates/admin/admin-edit.html
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/admins";
        }
    }

    @PostMapping("/admins/update/{id}")
    public String updateAdmin(@PathVariable Long id,
                              @Valid @ModelAttribute("updateAdminRequest") UpdateAdminRequest request,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.updateAdminRequest", result);
            redirectAttributes.addFlashAttribute("updateAdminRequest", request);
            return "redirect:/admin/admins/edit/" + id;
        }
        try {
            adminService.updateAdmin(id, request); // <-- შეცვლილი ხაზი: გადაეცით request ობიექტი პირდაპირ
            redirectAttributes.addFlashAttribute("message", "Admin updated successfully!");
            return "redirect:/admin/admins";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/admins";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating admin: " + e.getMessage());
            return "redirect:/admin/admins/edit/" + id;
        }
    }

    @PostMapping("/admins/delete/{id}")
    public String deleteAdmin(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            adminService.deleteAdmin(id); // Assuming AdminService has deleteAdmin()
            redirectAttributes.addFlashAttribute("message", "Admin deleted successfully!");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting admin: " + e.getMessage());
        }
        return "redirect:/admin/admins";
    }
}