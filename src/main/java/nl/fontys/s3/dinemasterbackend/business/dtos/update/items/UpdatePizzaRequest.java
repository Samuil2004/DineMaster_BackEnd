package nl.fontys.s3.dinemasterbackend.business.dtos.update.items;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.dinemasterbackend.domain.classes.PizzaSize;

import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdatePizzaRequest extends UpdateItemRequest {

    @NotNull(message = "Sizes cannot be null.")
    @NotEmpty(message = "At least one pizza size must be provided.")
    private List<PizzaSize> sizes;

    @NotBlank(message = "Base cannot be blank.")
    private String base;
}
