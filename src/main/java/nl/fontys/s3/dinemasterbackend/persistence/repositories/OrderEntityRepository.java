package nl.fontys.s3.dinemasterbackend.persistence.repositories;

import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderStatusEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderEntityRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByOrderStatus(OrderStatusEntity orderStatus);

    List<OrderEntity> findByOrderStatusAndIsTaken(OrderStatusEntity orderStatus, Boolean isTaken);

    Optional<OrderEntity> findByOrderIdAndIsTaken(Long orderId, Boolean isTaken);

    @Query("SELECT o FROM OrderEntity o WHERE o.customerEntity.userId = :customerId AND o.orderStatus.id != 4")
    Optional<OrderEntity> findOrderByCustomerIdAndStatusNotDelivered(Long customerId);

    List<OrderEntity> findByCustomerEntityUserId(Long customerId, Pageable pageable);

    long countByCustomerEntityUserId(Long customerId);

}
