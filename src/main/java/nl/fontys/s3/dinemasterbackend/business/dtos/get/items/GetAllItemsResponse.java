package nl.fontys.s3.dinemasterbackend.business.dtos.get.items;

import lombok.Builder;
import lombok.Getter;
import nl.fontys.s3.dinemasterbackend.domain.classes.Item;
import java.util.List;

@Builder
@Getter
public class GetAllItemsResponse {
    private List<Item> allItems;
}
