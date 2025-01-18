package nl.fontys.s3.dinemasterbackend.persistence.repositories;

import nl.fontys.s3.dinemasterbackend.persistence.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByRoleName(String roleName, Pageable pageable);


    List<UserEntity> findByRole_NameAndLastNameContainingIgnoreCase(String roleName, String namePart, Pageable pageable);

    List<UserEntity> findByRole_nameAndEmailContainingIgnoreCase(String roleName, String namePart, Pageable pageable);

    long countByRoleName(String roleName);

    long countByRole_NameAndLastNameContainingIgnoreCase(String roleName, String lastNamePart);

    long countByRole_nameAndEmailContainingIgnoreCase(String roleName, String emailPart);
}
