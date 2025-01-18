package nl.fontys.s3.dinemasterbackend.business.item_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.delete.DeleteItemRequest;

public interface DeleteItemById {
    void deleteItemById(DeleteItemRequest deleteItemRequest);

    void deleteItemFromOrders(DeleteItemRequest deleteItemRequest);


}
