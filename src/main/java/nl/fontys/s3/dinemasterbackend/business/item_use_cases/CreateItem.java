package nl.fontys.s3.dinemasterbackend.business.item_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.create.items.CreateItemRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.items.CreateItemResponse;

public interface CreateItem {
    CreateItemResponse createItem(CreateItemRequest itemRequest);
}
