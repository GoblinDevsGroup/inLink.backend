package org.example.adds.Notification;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotificationToUser(UUID userId, String message) {

        //todo: save a notification to db
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/notify/sms",
                message);
    }
}
