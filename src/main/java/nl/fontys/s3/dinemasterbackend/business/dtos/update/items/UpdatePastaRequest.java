package nl.fontys.s3.dinemasterbackend.business.dtos.update.items;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdatePastaRequest extends UpdateItemRequest {
    @NotBlank(message = "Pasta type cannot be blank.")
    private String pastaType;

    @NotNull(message = "Weight cannot be empty.")
    @Min(value = 1, message = "Weight must be at least 1 gram.")
    @Max(value = 10000, message = "Weight must not exceed 10000 grams.")
    private Integer weight;
}
