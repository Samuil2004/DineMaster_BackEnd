package nl.fontys.s3.dinemasterbackend.business.order_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetCartRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetCartResponse;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;

public interface GetCart {
    GetCartResponse getCart(GetCartRequest request, AccessToken accessToken);
}
