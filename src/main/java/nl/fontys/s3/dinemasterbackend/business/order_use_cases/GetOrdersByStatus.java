package nl.fontys.s3.dinemasterbackend.business.order_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrderByStatusRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrdersResponse;

public interface GetOrdersByStatus {
    GetOrdersResponse getOrdersByStatus(GetOrderByStatusRequest request);
}
