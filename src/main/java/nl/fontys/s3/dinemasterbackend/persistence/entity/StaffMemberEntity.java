package nl.fontys.s3.dinemasterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "staff_members")
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode
public class StaffMemberEntity extends UserEntity {
    @Column(name = "staff_id", unique = true, nullable = false)
    private Long staffId;
}
