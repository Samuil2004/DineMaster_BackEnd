package nl.fontys.s3.dinemasterbackend.business.converters;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.domain.classes.Order;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderConverter {
    private final CartConverter cartConverter;
    private final UserConverter userConverter;
    private final OrderStatusConverter orderStatusConverter;

    public Order convertEntityToNormal(OrderEntity entity) {
        return Order.builder()
                .orderId(entity.getOrderId())
                .cart(cartConverter.convertEntityToNormal(entity.getCartEntity()))
                .comments(entity.getComments())
                .customerWhoOrdered(userConverter.convertEntityToCustomer(entity.getCustomerEntity()))
                .orderStatus(orderStatusConverter.convertEntityToNormal(entity.getOrderStatus()))
                .deliveryFee(entity.getDeliveryFee())
                .isTaken(entity.getIsTaken())
                .build();
    }

    public OrderEntity convertNormalToEntity(Order order) {
        return OrderEntity.builder()
                .orderId(order.getOrderId())
                .cartEntity(cartConverter.convertNormalToEntity(order.getCart()))
                .comments(order.getComments())
                .customerEntity(userConverter.convertCustomerToEntity(order.getCustomerWhoOrdered()))
                .orderStatus(orderStatusConverter.convertNormalToEntity(order.getOrderStatus()))
                .deliveryFee(order.getDeliveryFee())
                .isTaken(order.getIsTaken())
                .build();
    }
}
