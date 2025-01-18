package nl.fontys.s3.dinemasterbackend.business.dtos.create.items;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode
public class CreateAppetizerRequest extends CreateItemRequest {
    @NotNull
    private Boolean isVegetarian;
}
