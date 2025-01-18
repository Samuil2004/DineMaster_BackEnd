package nl.fontys.s3.dinemasterbackend.business.dtos.notfications;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NotificationMessage {
    private String id;
    private String from;
    private Long to;
    private String text;
}
