package nl.fontys.s3.dinemasterbackend.business.dtos.update.items;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UpdateBeverageRequest extends UpdateItemRequest {
    @NotNull(message = "Size must not be empty.")
    @DecimalMin(value = "0.0", message = "Size must be at least 0.0.")
    @DecimalMax(value = "10.0", message = "Size must not exceed 10.0.")
    @Digits(integer = 2, fraction = 2, message = "Size must be a valid decimal number with up to 2 digits in total and 2 digits after the decimal point.")
    private Double size;
}
