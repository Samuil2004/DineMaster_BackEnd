package nl.fontys.s3.dinemasterbackend.business.dtos.create.orders;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddItemDifferentFromPizzaToCartRequest extends AddItemToCartRequest {
    @NotNull(message = "Item id must not be null")
    @Min(value = 1, message = "Item id must be a positive number")
    private Long selectedItemId;

}
