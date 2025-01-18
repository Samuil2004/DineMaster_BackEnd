package nl.fontys.s3.dinemasterbackend.domain.classes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class Cart {
    private Long cartId;
    private Long customerId;
    private Double price;
    private List<SelectedItem> selectedItems;
    private Boolean isActive;
}
