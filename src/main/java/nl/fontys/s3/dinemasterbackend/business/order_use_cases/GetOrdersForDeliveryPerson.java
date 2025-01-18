package nl.fontys.s3.dinemasterbackend.business.order_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.GetOrdersForDeliveryPersonResponse;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrdersForDeliveryPersonRequest;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;

public interface GetOrdersForDeliveryPerson {
    GetOrdersForDeliveryPersonResponse getActiveOrdersForDeliveryPerson(GetOrdersForDeliveryPersonRequest request, AccessToken accessToken);

}
