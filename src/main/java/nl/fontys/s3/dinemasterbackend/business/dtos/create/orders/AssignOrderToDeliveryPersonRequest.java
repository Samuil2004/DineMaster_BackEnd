package nl.fontys.s3.dinemasterbackend.business.dtos.create.orders;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignOrderToDeliveryPersonRequest {

    @NotNull(message = "Order id cannot be null")
    @Min(value = 1, message = "Order id cannot be less than 1")
    private Long orderId;

    @NotNull(message = "User id cannot be null")
    @Min(value = 1, message = "User id cannot be less than 1")
    private Long deliveryPersonId;
}
