package com.example.bookingtour.controller.admin;

import com.example.bookingtour.entity.Notification;
import com.example.bookingtour.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/admin/notification")
    public String showNotificationPage(Model model,
                                       @RequestParam(value = "searchTerm", required = false) String searchTerm,
                                       @RequestParam(value = "resultsPerPage", required = false, defaultValue = "10") int resultsPerPage,
                                       @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                       @RequestParam(value = "sort", defaultValue = "id", required = false) String sort,
                                       @RequestParam(value = "order", defaultValue = "asc", required = false) String order
    ) {
        List<Object[]> notifications;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            notifications = notificationService.searchNotifications(searchTerm);
        } else {
            notifications = notificationService.getAllNotifications();
        }
//        for (Object[] row : notifications) {
//            // In ra các giá trị của từng cột trong Object[]
//            System.out.println("Notification ID: " + row[0]);
//            System.out.println("Message: " + row[1]);
//            System.out.println("Send Date: " + row[2]);
//            System.out.println("Status: " + row[3]);
//            System.out.println("Customer Name: " + row[4]);
//            System.out.println("Customer ID: " + row[5]);
//            System.out.println("-------------");
//        }
        if (notifications.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy kết quả phù hợp");
        }
        if ("asc".equals(order)) {
            notifications.sort((c1, c2) -> {
                Comparable value1 = getPropertyValue(c1, sort);
                Comparable value2 = getPropertyValue(c2, sort);
                return value1 == null ? -1 : value1.compareTo(value2);
            });
        } else {
            notifications.sort((c1, c2) -> {
                Comparable value1 = getPropertyValue(c1, sort);
                Comparable value2 = getPropertyValue(c2, sort);
                return value2 == null ? -1 : value2.compareTo(value1);
            });
        }


        int totalNotifications = notifications.size();
        int totalPages = (int) Math.ceil((double) totalNotifications / resultsPerPage);
        int start = (page - 1) * resultsPerPage;
        int end = Math.min(start + resultsPerPage, totalNotifications);

        List<Object[]> paginatedNotifications = notifications.subList(start, end);

        model.addAttribute("notifications", paginatedNotifications);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("resultsPerPage", resultsPerPage);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        return "admin/notification/index";
    }

    private Comparable<?> getPropertyValue(Object[] n, String propertyName) {
        switch (propertyName) {
            case "id":
                return (Comparable<?>) n[0];
            case "nameCus":
                return (Comparable<?>) n[4];
            case "content":
                return (Comparable<?>) n[1];
            case "send_date":
                return (Comparable<?>) n[2];
            default:
                return null;
        }
    }

    @DeleteMapping("/admin/notification/delete/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable("notificationId") int notificationId) {
        try {
            notificationService.deleteNotification(notificationId);
            return ResponseEntity.ok("Xóa thông báo thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xóa thông báo thất bại.");
        }
    }

    @GetMapping("admin/notification/edit/{id}")
    public String showPageEditNotification(@PathVariable("id") int id, Model model) {
        Notification n = notificationService.findById(id);

        model.addAttribute("notification", n);
        return "admin/notification/edit";
    }

    @PostMapping("admin/notification/update/{id}/send")
    public String executeEditNotification(@PathVariable("id") int id, @ModelAttribute("notification") Notification notification, RedirectAttributes ra) {

        Notification n = notificationService.findById(id);
        n.setMessage(notification.getMessage());
        // Lưu khách hàng đã cập nhật
        notificationService.saveNotification(n);
        ra.addFlashAttribute("message", "Cập nhật thành công");
        return String.format("redirect:/admin/notification/edit/%d", id);
    }


}
