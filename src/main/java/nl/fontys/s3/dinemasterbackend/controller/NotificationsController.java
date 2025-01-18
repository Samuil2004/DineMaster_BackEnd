package nl.fontys.s3.dinemasterbackend.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.notfications.NotificationMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@AllArgsConstructor
public class NotificationsController {
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public ResponseEntity<Void> sendNotificationToUsers(@RequestBody NotificationMessage message) {
        messagingTemplate.convertAndSend("/topic/publicmessages", message);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
