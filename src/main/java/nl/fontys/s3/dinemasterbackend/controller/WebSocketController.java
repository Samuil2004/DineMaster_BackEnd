package nl.fontys.s3.dinemasterbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.notfications.NotificationMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("notifications")
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Endpoint for sending a private message to a user via WebSocket.
     *
     * @param message The notification message to send to the user.
     * @return A response indicating that the message was successfully sent.
     */
    @Operation(
            summary = "Send private message",
            description = "This endpoint sends a private WebSocket message to a specified user. " +
                    "The message is sent to the recipient's private inbox using the WebSocket protocol.",
            tags = {"WebSocket Messaging"}
    )
    @PostMapping("/send-private-message")
    public void sendPrivateMessage(
            @Parameter(description = "The notification message to send to the user")
            @RequestBody NotificationMessage message) {

        String destination = "/user/" + message.getTo() + "/queue/inboxmessages";
        messagingTemplate.convertAndSend(destination, message);
    }
}
