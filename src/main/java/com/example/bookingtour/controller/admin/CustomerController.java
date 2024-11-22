package com.example.bookingtour.controller.admin;

import com.example.bookingtour.entity.Customer;
import com.example.bookingtour.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;



    @GetMapping("/admin/customer")
    public String showCustomerPage(Model model,
                                   @RequestParam(value = "searchTerm", required = false) String searchTerm,
                                   @RequestParam(value = "resultsPerPage", required = false, defaultValue = "10") int resultsPerPage,
                                   @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                   @RequestParam(value = "sort", defaultValue = "name", required = false) String sort,
                                   @RequestParam(value = "order", defaultValue = "asc", required = false) String order
    ) {
        List<Customer> customers;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            customers = customerService.searchCustomers(searchTerm);
        } else {
            customers = customerService.getAllCustomers();
        }
        if (customers.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy kết quả phù hợp");
        }
        if ("asc".equals(order)) {
            customers.sort((c1, c2) -> {
                Comparable value1 = getPropertyValue(c1, sort);
                Comparable value2 = getPropertyValue(c2, sort);
                return value1 == null ? -1 : value1.compareTo(value2);
            });
        } else {
            customers.sort((c1, c2) -> {
                Comparable value1 = getPropertyValue(c1, sort);
                Comparable value2 = getPropertyValue(c2, sort);
                return value2 == null ? -1 : value2.compareTo(value1);
            });
        }


        int totalCustomers = customers.size();
        int totalPages = (int) Math.ceil((double) totalCustomers / resultsPerPage);
        int start = (page - 1) * resultsPerPage;
        int end = Math.min(start + resultsPerPage, totalCustomers);

        List<Customer> paginatedCustomers = customers.subList(start, end);

        model.addAttribute("customers", paginatedCustomers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("resultsPerPage", resultsPerPage);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        return "admin/customer/cus_table";
    }

    private Comparable<?> getPropertyValue(Customer customer, String propertyName) {
        if ("name".equals(propertyName)) {
            return customer.getName(); // String
        } else if ("email".equals(propertyName)) {
            return customer.getEmail(); // String
        } else if ("status".equals(propertyName)) {
            return customer.isStatus() ? 1 : 0;
        }
        return null;
    }

    @PostMapping("admin/customer/delete/{customerId}")
    @ResponseBody
    public ResponseEntity<String> deleteCustomer(@PathVariable("customerId") int customerId) {
        try {
            customerService.deleteCustomer(customerId);
            return ResponseEntity.ok("Xóa khách hàng thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xóa khách hàng thất bại.");
        }
    }

    @PostMapping("admin/customer/restore/{customerId}")
    @ResponseBody
    public ResponseEntity<String> restoreCustomer(@PathVariable("customerId") int customerId) {
        try {
            customerService.restoreCustomer(customerId);
            return ResponseEntity.ok("Khôi phục khách hàng thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Khôi phục khách hàng thất bại.");
        }
    }

    @GetMapping("admin/customer/edit/{id}")
    public String showPageEditCustomer(@PathVariable("id") int id, Model model) {
        Customer c = this.customerService.findById(id);

        model.addAttribute("customer", c);
        return "admin/customer/cus_edit";
    }

    @PostMapping("admin/customer/update/{id}/send")
    public String executeEditCustomer(@PathVariable("id") int id, @ModelAttribute Customer customer, RedirectAttributes ra) {
        Map<String, String> errors = validateCustomer(customer);

        if (!errors.isEmpty()) {
            errors.forEach((key, value) -> ra.addFlashAttribute("message", value));
            return String.format("redirect:/admin/customer/edit/%d", id);
        }

        Customer c = this.customerService.findById(id);
        c.setName(customer.getName());
        c.setEmail(customer.getEmail());
        c.setPhone_number(customer.getPhone_number());
        c.setAddress(customer.getAddress());
        c.setStatus(customer.isStatus());

        // Lưu khách hàng đã cập nhật
        customerService.save(c);
        ra.addFlashAttribute("message", "Cập nhật thành công");
        return String.format("redirect:/admin/customer/edit/%d", id);
    }

    @GetMapping("admin/customer/add")
    public String showPageAddCustomer(Model model) {
        model.addAttribute("customer", new Customer());
        return "admin/customer/cus_add";
    }

    @PostMapping("admin/customer/add/send")
    public String executeAddCustomer(@ModelAttribute Customer customer, RedirectAttributes ra) {
        Map<String, String> errors = validateCustomer(customer);

        if (!errors.isEmpty()) {
            errors.forEach((key, value) -> ra.addFlashAttribute("message", value));
            return "redirect:/admin/customer/add";
        }

        customer.setPassword("1");
        customerService.save(customer);
        ra.addFlashAttribute("message", "Thêm mới khách hàng thành công");
        return "redirect:/admin/customer/add";
    }

    private Map<String, String> validateCustomer(Customer customer) {
        Map<String, String> errors = new HashMap<>();

        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            errors.put("nameError", "Tên khách hàng không được để trống");
        }

        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty() || !customer.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.put("emailError", "Email không hợp lệ");
        }

        if (customer.getPhone_number() == null || customer.getPhone_number().trim().isEmpty() || !customer.getPhone_number().matches("^(0[3|5|7|8|9])+([0-9]{8})$")) {
            errors.put("phoneError", "Số điện thoại không hợp lệ");
        }

        if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
            errors.put("addressError", "Địa chỉ không được để trống");
        }

        return errors;
    }


}
