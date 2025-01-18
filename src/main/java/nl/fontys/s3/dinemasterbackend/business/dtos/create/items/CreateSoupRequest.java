package nl.fontys.s3.dinemasterbackend.business.dtos.create.items;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CreateSoupRequest extends CreateItemRequest {
    @NotNull(message = "The vegetarian status must be specified.")
    private Boolean isVegetarian;
}
