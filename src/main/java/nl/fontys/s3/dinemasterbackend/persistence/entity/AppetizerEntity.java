package nl.fontys.s3.dinemasterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "appetizers")
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(callSuper = true)
public class AppetizerEntity extends ItemEntity {
    @Column(name = "is_vegetarian")
    @EqualsAndHashCode.Include
    private Boolean isVegetarian;
}