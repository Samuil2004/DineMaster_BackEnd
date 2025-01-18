package nl.fontys.s3.dinemasterbackend.business.dtos.delete;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class DeleteItemFromCartRequest {
    @NotNull(message = "Cart ID must not be null.")
    @Min(value = 1, message = "Cart ID must be at least 1.")
    private Long cartId;

    @NotNull(message = "Selected Item ID must not be null.")
    @Min(value = 1, message = "Selected Item ID must be at least 1.")
    private Long selectedItemId;
}
