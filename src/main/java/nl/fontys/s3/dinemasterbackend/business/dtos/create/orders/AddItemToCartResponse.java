package nl.fontys.s3.dinemasterbackend.business.dtos.create.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.dinemasterbackend.domain.classes.Cart;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddItemToCartResponse {
    private Cart cart;
}
