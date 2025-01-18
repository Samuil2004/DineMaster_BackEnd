package nl.fontys.s3.dinemasterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pizza_sizes")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PizzaSizeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "size_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "size", nullable = false)
    @EqualsAndHashCode.Include
    private String size;

    @Column(name = "additional_price", nullable = false)
    @EqualsAndHashCode.Include
    private Double additionalPrice;

    @ManyToOne
    @JoinColumn(name = "pizza_id", nullable = false)
    private PizzaEntity pizza;
}
