package com.example.bookingtour.service;

import com.example.bookingtour.entity.Notification;
import com.example.bookingtour.repository.NotificationRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Object[]>getAllNotifications(){
        return  notificationRepository.getAllNotifications();
    }
    public List<Object[]> searchNotifications(String searchTerm){
        return  notificationRepository.searchNotifications(searchTerm);
    }
    public void deleteNotification(int notificationId) {
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if (notification.isPresent()) {
            notificationRepository.deleteById(notificationId);
        } else {
            throw new RuntimeException("Thông báo không tồn tại");
        }
    }
    public Notification findById(int notificationId) {
        Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
        return notificationOptional.isPresent() ? notificationOptional.get() : null;
    }
    public void saveNotification(Notification n){
        notificationRepository.save(n);
    }
   public List<Notification> getAlls(){
        return  notificationRepository.findAll();
   }
}
