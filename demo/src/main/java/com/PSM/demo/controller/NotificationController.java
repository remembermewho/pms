package com.PSM.demo.controller;

import com.PSM.demo.model.Notification;
import com.PSM.demo.model.UserEntity;
import com.PSM.demo.service.NotificationService;
import com.PSM.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @Autowired
    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("/notifications")
    public String viewNotifications(Model model, Authentication authentication) {
        UserEntity currentUser = userService.findByUsername(authentication.getName());
        List<Notification> notifications = notificationService.getUnreadNotifications(currentUser);
        model.addAttribute("notifications", notifications);
        return "notifications";
    }
}
