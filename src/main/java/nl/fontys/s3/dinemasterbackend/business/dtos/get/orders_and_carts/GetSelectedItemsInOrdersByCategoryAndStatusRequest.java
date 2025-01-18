package nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetSelectedItemsInOrdersByCategoryAndStatusRequest {

    private String category;
    private String status;
}
