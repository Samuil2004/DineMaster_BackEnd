package nl.fontys.s3.dinemasterbackend.business.order_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.CheckForCustomerActiveCartRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.CheckForCustomerActiveCartResponse;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;

public interface CheckActiveCartForCustomer {
    CheckForCustomerActiveCartResponse checkActiveCart(CheckForCustomerActiveCartRequest request, AccessToken accessToken);
}
