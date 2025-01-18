package nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetOrdersByCategoryAndStatusRequest {
    private String category;
    private String status;
}
