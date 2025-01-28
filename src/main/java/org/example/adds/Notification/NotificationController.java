package org.example.adds.Notification;

import lombok.AllArgsConstructor;
import org.example.adds.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/notify")
@AllArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<Response> sendNotification(@RequestParam UUID userId,
                                                     @RequestParam String message) {
        notificationService.sendNotificationToUser(userId, message);
        return ResponseEntity.ok(new Response("notification sent", true));
    }
}
