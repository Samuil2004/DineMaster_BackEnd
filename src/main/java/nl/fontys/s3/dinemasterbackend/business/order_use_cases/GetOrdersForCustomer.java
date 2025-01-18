package nl.fontys.s3.dinemasterbackend.business.order_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrdersForCustomerRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrdersResponse;

public interface GetOrdersForCustomer {
    GetOrdersResponse getOrdersForCustomer(GetOrdersForCustomerRequest request);
}
