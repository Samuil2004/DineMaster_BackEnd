package nl.fontys.s3.dinemasterbackend.business.item_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.get.items.GetItemsForCategoryRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.items.GetItemsForCategoryResponse;

public interface GetItemsFromCategory {
    GetItemsForCategoryResponse readItemsByCategory(GetItemsForCategoryRequest request);

}
