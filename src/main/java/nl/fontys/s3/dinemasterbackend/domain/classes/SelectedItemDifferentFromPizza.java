package nl.fontys.s3.dinemasterbackend.domain.classes;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class SelectedItemDifferentFromPizza extends SelectedItem {
    private Item itemFromMenu;
}
