package nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetOrdersForDeliveryPersonRequest {
    @NotNull(message = "User id cannot be null")
    @Min(value = 1, message = "User id cannot be less than 1")
    private Long deliveryPersonId;

    @NotNull(message = "Page number should not be null")
    @Min(value = 1, message = "Page number should not be less than 1")
    private Integer pageNumber;

    @NotNull(message = "Status Id should not be null")
    @Min(value = 1, message = "Status Id should not be less than 1")
    private Long statusId;
}
