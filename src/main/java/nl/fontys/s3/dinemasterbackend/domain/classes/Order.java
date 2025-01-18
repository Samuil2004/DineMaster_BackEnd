package nl.fontys.s3.dinemasterbackend.domain.classes;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class Order {
    private Long orderId;
    private String comments;
    private Cart cart;
    private OrderStatus orderStatus;
    private Customer customerWhoOrdered;
    private Double deliveryFee;
    private Boolean isTaken;
}
