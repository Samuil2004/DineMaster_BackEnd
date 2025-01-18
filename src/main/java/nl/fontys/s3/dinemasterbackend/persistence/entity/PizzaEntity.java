package nl.fontys.s3.dinemasterbackend.persistence.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pizzas")
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(callSuper = true)
public class PizzaEntity extends ItemEntity {

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "pizza_id")
    @EqualsAndHashCode.Exclude
    private List<PizzaSizeEntity> sizes;

    @Column(name = "base", nullable = false)
    private String base;
}
