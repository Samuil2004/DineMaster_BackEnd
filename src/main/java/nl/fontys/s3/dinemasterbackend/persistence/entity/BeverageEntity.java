package nl.fontys.s3.dinemasterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "beverages")
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(callSuper = true)
public class BeverageEntity extends ItemEntity {
    @Column(name = "size", nullable = false)
    @EqualsAndHashCode.Include
    private Double size;
}