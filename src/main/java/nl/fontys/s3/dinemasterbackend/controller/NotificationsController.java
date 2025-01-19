package nl.fontys.s3.dinemasterbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    /**
     * Endpoint for sending a notification to users.
     *
     * @param message The notification message to send to users.
     * @return A 201 Created response indicating the notification was successfully sent.
     */
    @Operation(
            summary = "Send a notification to users",
            description = "This endpoint sends a notification message to all subscribed users. " +
                    "The message is sent to the '/topic/publicmessages' channel via WebSocket.",
            tags = {"Notification Management"}
    )
    @PostMapping
    public ResponseEntity<Void> sendNotificationToUsers(
            @Parameter(description = "The notification message to send to users")
            @RequestBody NotificationMessage message) {

        messagingTemplate.convertAndSend("/topic/publicmessages", message);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
