package nl.fontys.s3.dinemasterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "grills")
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(callSuper = true)
public class GrillEntity extends ItemEntity {
    @Column(name = "weight", nullable = false)
    @EqualsAndHashCode.Include
    private Integer weight;
}
