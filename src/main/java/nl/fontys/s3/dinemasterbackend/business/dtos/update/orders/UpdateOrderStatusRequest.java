package nl.fontys.s3.dinemasterbackend.business.dtos.update.orders;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
public class UpdateOrderStatusRequest {

    @NotNull(message = "Order ID must not be null.")
    @Min(value = 1, message = "Order ID must be at least 1.")
    private Long orderId;

    @NotNull(message = "Status must not be null.")
    @NotBlank(message = "Status must not be blank.")
    private String status;
}
