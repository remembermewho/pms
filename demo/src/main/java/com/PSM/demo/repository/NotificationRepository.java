package com.PSM.demo.repository;

import com.PSM.demo.model.Notification;
import com.PSM.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientAndIsReadFalse(UserEntity recipient);
}
