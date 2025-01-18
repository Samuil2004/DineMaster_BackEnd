package nl.fontys.s3.dinemasterbackend.domain.classes;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Getter
@Setter
public class SelectedPizza extends SelectedItem{
    private String itemName;
    private String itemImageVersion;
    private Double itemPrice;
    private Long referencedPizzaId;
    private PizzaSize sizes;
    private String base;
}
