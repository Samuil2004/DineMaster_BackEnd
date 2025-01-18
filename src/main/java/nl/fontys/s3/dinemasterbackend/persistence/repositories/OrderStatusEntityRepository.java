package nl.fontys.s3.dinemasterbackend.persistence.repositories;

import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStatusEntityRepository extends JpaRepository<OrderStatusEntity, Long> {
    Optional<OrderStatusEntity> findByStatusName(String statusName);
}
