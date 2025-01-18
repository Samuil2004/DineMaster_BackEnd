package nl.fontys.s3.dinemasterbackend.business.order_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.AssignOrderToDeliveryPersonRequest;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;

public interface AssignOrderToDeliveryPerson {
    void assignOrderToDeliveryPerson(AssignOrderToDeliveryPersonRequest request, AccessToken accessToken);

}
