package nl.fontys.s3.dinemasterbackend.persistence.repositories;

import nl.fontys.s3.dinemasterbackend.persistence.entity.SelectedItemStatusOfPreparationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SelectedItemStatusOfPreparationEntityRepository extends JpaRepository<SelectedItemStatusOfPreparationEntity, Long> {
    Optional<SelectedItemStatusOfPreparationEntity> findByStatusName(String statusName);
}
