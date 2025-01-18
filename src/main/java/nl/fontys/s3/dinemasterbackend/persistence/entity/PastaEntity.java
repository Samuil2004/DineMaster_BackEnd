package nl.fontys.s3.dinemasterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "pastas")
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(callSuper = true)
public class PastaEntity extends ItemEntity {
    @Column(name = "pasta_type", nullable = false)
    private String pastaType;

    @Column(name = "weight", nullable = false)
    private Integer weight;
}
