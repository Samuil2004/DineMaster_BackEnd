package nl.fontys.s3.dinemasterbackend.business.dtos.get.items;

import lombok.Builder;
import lombok.Getter;
import nl.fontys.s3.dinemasterbackend.domain.classes.Item;

@Builder
@Getter
public class GetItemByIdResponse {
    private Item foundItem;
}
