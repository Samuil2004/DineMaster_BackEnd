package nl.fontys.s3.dinemasterbackend.persistence.repositories;

import nl.fontys.s3.dinemasterbackend.persistence.entity.ItemCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategoryEntity, Long> {
    Optional<ItemCategoryEntity> findByCategoryName(String categoryName);
}
