package nl.fontys.s3.dinemasterbackend.persistence.repositories;

import nl.fontys.s3.dinemasterbackend.persistence.entity.ItemEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.SelectedItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectedItemEntityRepository extends JpaRepository<SelectedItemEntity, Long> {
    void deleteBySelectedItemId(Long selectedItemId);

    @Query("SELECT si FROM SelectedItemEntity si " +
            "JOIN si.cart c " +
            "WHERE si.itemOfReference = :itemReference AND c.isActive = true")
    List<SelectedItemEntity> findSelectedItemsInActiveCarts(@Param("itemReference") ItemEntity itemReference);

    List<SelectedItemEntity> findByStatusOfPreparation_IdAndItemCategory_CategoryId(Long preparationStatusId, Long categoryId);

    @Modifying
    @Query("DELETE FROM SelectedItemEntity s WHERE s.itemOfReference.itemId = :itemReferenceId")
    void deleteByItemOfReferenceId(@Param("itemReferenceId") Long itemReferenceId);
}
