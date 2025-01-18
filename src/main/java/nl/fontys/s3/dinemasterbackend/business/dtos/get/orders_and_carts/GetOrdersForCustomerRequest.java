package nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetOrdersForCustomerRequest {
    @NotNull(message = "Customer Id should not be null")
    @Min(value = 1, message = "Customer id should not be less than 1")
    private Long customerId;

    @NotNull(message = "Page number should not be null")
    @Min(value = 1, message = "Page number should not be less than 1")
    private Integer pageNumber;
}
