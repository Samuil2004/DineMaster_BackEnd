package nl.fontys.s3.dinemasterbackend.business.item_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.get.items.GetItemByIdRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.items.GetItemByIdResponse;

public interface GetItemById {
    GetItemByIdResponse getItemById(GetItemByIdRequest request);
}
