package nl.fontys.s3.dinemasterbackend.business.order_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrderByIdRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrderByIdResponse;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderEntity;

public interface GetOrderById {
    GetOrderByIdResponse getOrderById(GetOrderByIdRequest request);

    OrderEntity getOrderByIdInternalService(Long orderId);

    OrderEntity getOrderByIdAndIsTaken(Long orderId, Boolean isTaken);
}
