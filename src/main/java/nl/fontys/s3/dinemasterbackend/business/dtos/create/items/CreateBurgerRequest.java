package nl.fontys.s3.dinemasterbackend.business.dtos.create.items;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode
public class CreateBurgerRequest extends CreateItemRequest {
    @NotNull(message = "Weight must not be empty.")
    @Min(value = 1, message = "Weight must be at least 1 gram.")
    @Max(value = 10000, message = "Weight must not exceed 10000 grams.")
    private Integer weight;
}
