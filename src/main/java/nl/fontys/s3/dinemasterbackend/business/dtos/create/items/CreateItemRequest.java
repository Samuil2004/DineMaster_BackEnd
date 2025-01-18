package nl.fontys.s3.dinemasterbackend.business.dtos.create.items;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CreateItemRequest {
    @NotBlank(message = "Item name cannot be blank.")
    private String itemName;


    @NotNull(message = "Item price cannot be empty.")
    @DecimalMin(value = "0.0", message = "Price must be at least 0.0.")
    @DecimalMax(value = "1000.0", message = "Price must not exceed 1000.0.")
    @Digits(integer = 4, fraction = 2, message = "Price must be a valid decimal number with up to 4 digits in total and 2 digits after the decimal point.")
    private Double itemPrice;

    @NotNull(message = "Ingredients list cannot be empty.")
    @NotEmpty(message = "Ingredients list cannot be empty.")
    private List<@NotBlank(message = "Ingredients are not added") String> ingredients;


    @NotNull(message = "Visibility in menu should not be null")
    private Boolean visibleInMenu;
}
