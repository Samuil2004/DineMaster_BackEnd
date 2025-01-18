package nl.fontys.s3.dinemasterbackend.business.dtos.create.items;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.dinemasterbackend.domain.classes.PizzaSize;

import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreatePizzaRequest extends CreateItemRequest {
    @NotNull(message = "Sizes cannot be null.")
    @NotEmpty(message = "At least one pizza size must be provided.")
    @Valid
    private List<PizzaSize> sizes;

    @NotBlank(message = "Base cannot be blank.")
    private String base;
}
