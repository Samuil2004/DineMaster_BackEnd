package nl.fontys.s3.dinemasterbackend.business.order_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.update.orders.UpdateOrderStatusRequest;

public interface UpdateOrderStatus {
    void updateOrderStatus(UpdateOrderStatusRequest request);
}
