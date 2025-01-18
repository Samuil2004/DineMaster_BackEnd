package nl.fontys.s3.dinemasterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "selected_items")
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.JOINED)
public class SelectedItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "selected_item_id")
    private Long selectedItemId;

    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "item_reference_id", nullable = false)
    private ItemEntity itemOfReference;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ItemCategoryEntity itemCategory;

    @Column(name = "comment", nullable = false)
    private String comment;

    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "preparation_status_id")
    private SelectedItemStatusOfPreparationEntity statusOfPreparation;
}
