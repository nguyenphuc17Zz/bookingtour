package com.example.bookingtour.controller;

import com.example.bookingtour.entity.Notification;
import com.example.bookingtour.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class NotificationPageController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification")
    public String showUserProfilePage(@RequestParam("id") int id, Model model) {

        List<Notification> notifications = notificationService.getAlls();
        List<Notification> filteredNotifications = notifications.stream()
                .filter(t -> t.getCustomer_id() == id)
                .toList();
        filteredNotifications=filteredNotifications.reversed();
        model.addAttribute("notifications", filteredNotifications);

        return "user/notification/notification";
    }
    @PutMapping("notification/read/{notificationId}")
    @ResponseBody
    public ResponseEntity<String> readNotification(@PathVariable("notificationId") int id) {
        try {
            Notification n = notificationService.findById(id);
            n.setStatus("read");
            notificationService.saveNotification(n);
            return ResponseEntity.ok("OK.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed.");
        }
    }
}
