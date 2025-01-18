package nl.fontys.s3.dinemasterbackend.business.order_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.AddItemToCartRequest;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;

public interface AddItemToCart {
    void addItemToCart(AddItemToCartRequest addItemToCartRequest, AccessToken accessToken);
}
