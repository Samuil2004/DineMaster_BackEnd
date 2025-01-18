package nl.fontys.s3.dinemasterbackend.controller;

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

    @PostMapping("/send-private-message")
    public void sendPrivateMessage(@RequestBody NotificationMessage message) {
        String destination = "/user/" + message.getTo() + "/queue/inboxmessages";
        messagingTemplate.convertAndSend(destination, message);
    }
}
