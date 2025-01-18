package nl.fontys.s3.dinemasterbackend.persistence.repositories;

import nl.fontys.s3.dinemasterbackend.persistence.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartEntityRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByCustomerIdAndIsActiveTrue(Long customerId);

}
