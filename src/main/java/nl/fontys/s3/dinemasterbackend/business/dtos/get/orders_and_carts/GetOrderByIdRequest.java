package nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetOrderByIdRequest {
    @NotNull
    private Long orderId;
}
