package nl.fontys.s3.dinemasterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "selected_pizza_entity")
public class SelectedPizzaEntity extends SelectedItemEntity {
    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "item_image_version")
    private String itemImageVersion;

    @Column(name = "item_price", nullable = false)
    private Double itemPrice;

    @ManyToOne
    @JoinColumn(name = "size_id", referencedColumnName = "size_id")
    private PizzaSizeEntity sizes;

    @Column(name = "base", nullable = false)
    private String base;

}