package nl.fontys.s3.dinemasterbackend.domain.classes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class SelectedItem {
    private Long selectedItemId;
    private Item itemOfReference;
    private Integer amount;
    private ItemCategory itemCategory;
    private String comment;
    private SelectedItemStatusOfPreparation statusOfPreparation;
}
