package nl.fontys.s3.dinemasterbackend.business.order_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.delete.DeleteItemFromCartRequest;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;

public interface DeleteItemFromCart {
    void deleteItemFromCart(DeleteItemFromCartRequest request, AccessToken accessToken);
}
