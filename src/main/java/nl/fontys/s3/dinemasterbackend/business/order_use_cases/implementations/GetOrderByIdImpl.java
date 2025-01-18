package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.OrderConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrderByIdRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrderByIdResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.GetOrderById;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetOrderByIdImpl implements GetOrderById {
    private final OrderEntityRepository orderEntityRepository;
    private final OrderConverter orderConverter;
    private static final String NOTFOUND_MESSAGE = "ORDER_NOT_FOUND";

    @Override
    public GetOrderByIdResponse getOrderById(GetOrderByIdRequest request) {
        Optional<OrderEntity> foundOrderById = orderEntityRepository.findById(request.getOrderId());
        if (foundOrderById.isPresent()) {
            return GetOrderByIdResponse.builder().order(orderConverter.convertEntityToNormal(foundOrderById.get())).build();
        }
        throw new NotFound(NOTFOUND_MESSAGE);
    }

    @Override
    public OrderEntity getOrderByIdInternalService(Long orderId) {
        Optional<OrderEntity> foundOrderById = orderEntityRepository.findById(orderId);
        if (foundOrderById.isPresent()) {
            return foundOrderById.get();
        }
        throw new NotFound(NOTFOUND_MESSAGE);
    }

    @Override
    public OrderEntity getOrderByIdAndIsTaken(Long orderId, Boolean isTaken) {
        Optional<OrderEntity> foundOrderById = orderEntityRepository.findByOrderIdAndIsTaken(orderId, isTaken);
        if (foundOrderById.isPresent()) {
            return foundOrderById.get();
        }
        throw new NotFound(NOTFOUND_MESSAGE);
    }


}
