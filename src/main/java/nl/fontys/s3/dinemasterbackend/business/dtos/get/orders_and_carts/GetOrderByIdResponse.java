package nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts;

import lombok.Builder;
import lombok.Getter;
import nl.fontys.s3.dinemasterbackend.domain.classes.Order;

@Builder
@Getter
public class GetOrderByIdResponse {
    private Order order;
}
