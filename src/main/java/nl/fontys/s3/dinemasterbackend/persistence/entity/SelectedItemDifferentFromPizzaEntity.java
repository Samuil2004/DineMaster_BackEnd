package nl.fontys.s3.dinemasterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "selected_item_different_from_pizza_entity")
public class SelectedItemDifferentFromPizzaEntity extends SelectedItemEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private ItemEntity itemFromMenu;
}

