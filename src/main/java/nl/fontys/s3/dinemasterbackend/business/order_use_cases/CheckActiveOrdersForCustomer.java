package nl.fontys.s3.dinemasterbackend.business.order_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.CheckForCustomerActiveOrderRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.CheckForCustomerActiveOrderResponse;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;

public interface CheckActiveOrdersForCustomer {
    CheckForCustomerActiveOrderResponse checkForCustomerActiveOrder(CheckForCustomerActiveOrderRequest checkForCustomerActiveOrderRequest, AccessToken accessToken);
}
