package nl.fontys.s3.dinemasterbackend.business.dtos.create.items;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CreatePastaRequest extends CreateItemRequest {
    @NotBlank(message = "Pasta type cannot be blank.")
    private String pastaType;

    @NotNull(message = "Weight cannot be empty.")
    @Min(value = 1, message = "Weight must be at least 1 gram.")
    @Max(value = 10000, message = "Weight must not exceed 10000 grams.")
    private Integer weight;
}
