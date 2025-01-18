package nl.fontys.s3.dinemasterbackend.persistence.repositories;

import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderDeliveryEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDeliveryRepository extends JpaRepository<OrderDeliveryEntity, Long> {
    @Query("SELECT od.order FROM OrderDeliveryEntity od " +
            "WHERE od.deliveryPerson.userId = :deliveryGuyId " +
            "AND od.order.orderStatus.id = :orderStatusId")
    List<OrderEntity> findOrdersForDeliveryPersonWithStatusForDeliveryPaginated(
            @Param("deliveryGuyId") Long deliveryGuyId,
            @Param("orderStatusId") Long orderStatusId,
            Pageable pageable);

    @Query("SELECT COUNT(od) FROM OrderDeliveryEntity od " +
            "WHERE od.deliveryPerson.userId = :deliveryGuyId " +
            "AND od.order.orderStatus.id = :orderStatusId")
    long countTotalOrdersForDeliveryPerson(@Param("deliveryGuyId") Long deliveryGuyId,
                                           @Param("orderStatusId") Long orderStatusId);
}
