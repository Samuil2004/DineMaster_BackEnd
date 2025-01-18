package nl.fontys.s3.dinemasterbackend.persistence.repositories;

import nl.fontys.s3.dinemasterbackend.persistence.entity.PizzaSizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PizzaSizeRepository extends JpaRepository<PizzaSizeEntity, Long> {
    List<PizzaSizeEntity> findByPizza_ItemId(Long pizzaId);
}