package nl.fontys.s3.dinemasterbackend.persistence.repositories;

import nl.fontys.s3.dinemasterbackend.persistence.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemEntityRepository extends JpaRepository<ItemEntity,Long> {
    boolean existsByItemName(String itemName);
    List<ItemEntity> findByItemCategory_CategoryId(Long categoryId);
    List<ItemEntity> findByItemCategory_CategoryIdAndVisibleInMenu(Long categoryId, boolean visibleInMenu);
}
