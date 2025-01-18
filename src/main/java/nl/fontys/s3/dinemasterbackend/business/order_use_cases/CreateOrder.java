package nl.fontys.s3.dinemasterbackend.business.order_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.CreateOrderRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.CreateOrderResponse;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;

public interface CreateOrder {
    CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest, AccessToken accessToken);
}
