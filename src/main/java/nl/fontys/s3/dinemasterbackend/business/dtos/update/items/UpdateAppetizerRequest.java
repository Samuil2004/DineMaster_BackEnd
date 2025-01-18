package nl.fontys.s3.dinemasterbackend.business.dtos.update.items;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateAppetizerRequest extends UpdateItemRequest {
    @NotNull(message = "The vegetarian status must be specified.")
    private Boolean isVegetarian;
}
