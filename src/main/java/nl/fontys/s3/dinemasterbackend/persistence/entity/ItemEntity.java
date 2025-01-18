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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "items")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    @EqualsAndHashCode.Include
    private Long itemId;

    @Column(name = "item_name", nullable = false)
    @EqualsAndHashCode.Include
    private String itemName;

    @EqualsAndHashCode.Exclude
    @Column(name = "item_image_url")
    private String itemImageUrl;

    @Column(name = "item_image_version")
    private String itemImageVersion;

    @Column(name = "item_price", nullable = false)
    private Double itemPrice;

    //The ingredients will be stored in a separate collection table named "item_ingredients" where there is going to be
    //two columns item_id (FK) and ingredient. The item entity will reference it and they will be joined by the item Id
    @ElementCollection
    @CollectionTable(name = "item_ingredients", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "ingredient")
    private List<String> ingredients;


    @Column(name = "visible_in_menu", nullable = false)
    private Boolean visibleInMenu;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @EqualsAndHashCode.Include
    private ItemCategoryEntity itemCategory;
}