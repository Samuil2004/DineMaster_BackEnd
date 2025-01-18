package nl.fontys.s3.dinemasterbackend.business.dtos.update.items;

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
public class UpdateSoupRequest extends UpdateItemRequest {
    @NotNull(message = "The vegetarian status must be specified.")
    private Boolean isVegetarian;
}
