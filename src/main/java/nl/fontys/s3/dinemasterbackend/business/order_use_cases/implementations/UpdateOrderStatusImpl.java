package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.update.orders.UpdateOrderStatusRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.UpdateOrderStatus;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderStatusEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderStatusEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateOrderStatusImpl implements UpdateOrderStatus {
    private final OrderEntityRepository orderEntityRepository;
    private final OrderStatusEntityRepository orderStatusEntityRepository;

    @Override
    @Transactional
    public void updateOrderStatus(UpdateOrderStatusRequest request) {
        Optional<OrderEntity> foundOrderEntity = orderEntityRepository.findById(request.getOrderId());
        if(foundOrderEntity.isPresent()) {
            Optional<OrderStatusEntity> foundStatus = orderStatusEntityRepository.findByStatusName(request.getStatus());
            if(foundStatus.isPresent()) {
                foundOrderEntity.get().setOrderStatus(foundStatus.get());
                orderEntityRepository.save(foundOrderEntity.get());
                return;
            }
            throw new NotFound("SELECTED STATUS NOT FOUND");
        }
        throw new NotFound("ORDER WAS NOT FOUND");
    }
}
