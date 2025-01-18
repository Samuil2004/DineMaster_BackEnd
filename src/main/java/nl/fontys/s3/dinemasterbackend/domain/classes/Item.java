package nl.fontys.s3.dinemasterbackend.domain.classes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode
public abstract class Item {
    private Long itemId;
    private String itemName;
    private String itemImageVersion;
    private String itemImageUrl;
    private Double itemPrice;
    private List<String> ingredients;
    private Boolean visibleInMenu;
    private ItemCategory itemCategory;
}
