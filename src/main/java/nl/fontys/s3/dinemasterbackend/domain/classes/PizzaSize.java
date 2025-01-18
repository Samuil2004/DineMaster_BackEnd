package nl.fontys.s3.dinemasterbackend.domain.classes;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@EqualsAndHashCode
public class PizzaSize {
    private Long pizzaSizeId;

    @NotBlank(message = "Pizza size name cannot be blank.")
    private String size;

    @NotNull(message = "Item price cannot be empty.")
    @DecimalMin(value = "0.0", message = "Price must be at least 0.0.")
    @DecimalMax(value = "1000.0", message = "Price must not exceed 1000.0.")
    @Digits(integer = 4, fraction = 2, message = "Price must be a valid decimal number with up to 4 digits in total and 2 digits after the decimal point.")
    private Double additionalPrice;
}
