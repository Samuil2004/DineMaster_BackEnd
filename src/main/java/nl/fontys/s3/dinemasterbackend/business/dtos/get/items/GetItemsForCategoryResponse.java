package nl.fontys.s3.dinemasterbackend.business.dtos.get.items;

import lombok.*;
import nl.fontys.s3.dinemasterbackend.domain.classes.Item;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class GetItemsForCategoryResponse {
    private List<Item> itemsInCategory;
}
