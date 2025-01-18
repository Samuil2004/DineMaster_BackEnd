package nl.fontys.s3.dinemasterbackend.business.dtos.create.orders;

import lombok.Builder;
import lombok.Getter;
import nl.fontys.s3.dinemasterbackend.domain.classes.Order;

@Builder
@Getter
public class CreateOrderResponse {
    private Order order;
}
