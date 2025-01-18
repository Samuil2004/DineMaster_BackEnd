package nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class CheckForCustomerActiveOrderRequest {
    @NotNull
    private Long customerId;
}
