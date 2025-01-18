package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.OrderConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrderByStatusRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrdersResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.GetOrdersByStatus;
import nl.fontys.s3.dinemasterbackend.domain.classes.Order;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderStatusEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderStatusEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetOrdersByStatusImpl implements GetOrdersByStatus {
    private final OrderEntityRepository orderEntityRepository;
    private final OrderStatusEntityRepository orderStatusEntityRepository;

    private final OrderConverter orderConverter;

    @Override
    public GetOrdersResponse getOrdersByStatus(GetOrderByStatusRequest request) {
        OrderStatusEntity foundStatus = findStatus(request.getStatus().toUpperCase());

        List<OrderEntity> foundOrders;
        if (request.getIsTaken() != null) {
            foundOrders = orderEntityRepository.findByOrderStatusAndIsTaken(foundStatus, request.getIsTaken());

        } else {
            foundOrders = orderEntityRepository.findByOrderStatus(foundStatus);
        }
        List<Order> allOrdersBase = foundOrders.stream().map(orderConverter::convertEntityToNormal).toList();

        return GetOrdersResponse.builder().allOrders(allOrdersBase).build();
    }

    private OrderStatusEntity findStatus(String status) {
        Optional<OrderStatusEntity> foundStatus = orderStatusEntityRepository.findByStatusName(status);
        if (foundStatus.isPresent()) {
            return foundStatus.get();
        }
        throw new NotFound("STATUS NOT FOUND");
    }
}
