package nl.fontys.s3.dinemasterbackend.persistence.repositories;

import nl.fontys.s3.dinemasterbackend.persistence.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenEntityRepository extends JpaRepository<RefreshTokenEntity, Long> {
    List<RefreshTokenEntity> findByUser_UserId(Long userId);

    Optional<RefreshTokenEntity> findByToken(String token);

}
