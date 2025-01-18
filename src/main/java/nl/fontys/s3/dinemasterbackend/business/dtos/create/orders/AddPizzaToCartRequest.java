package nl.fontys.s3.dinemasterbackend.business.dtos.create.orders;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddPizzaToCartRequest extends AddItemToCartRequest {

    @NotBlank(message = "Pizza size must not be blank.")
    private String pizzaSize;
}
