package nl.fontys.s3.dinemasterbackend.business.dtos.create.orders;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddItemToCartRequest {

    @NotNull(message = "Customer ID must not be null.")
    @Min(value = 1, message = "Customer ID must be a positive number greater than or equal to 1.")
    private Long customerId;

    @NotNull(message = "Quantity must not be null.")
    @Min(value = 1, message = "Quantity must be a positive number greater than or equal to 1.")
    @Max(value = 15, message = "Quantity must not exceed 15.")
    private Integer quantity;

    @NotNull(message = "Item reference ID must not be null.")
    @Min(value = 1, message = "Item reference ID must be a positive number greater than or equal to 1.")
    private Long itemOfReferenceId;

    private String comment;
}
