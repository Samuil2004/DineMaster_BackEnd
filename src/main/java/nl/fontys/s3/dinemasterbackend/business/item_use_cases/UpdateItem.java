package nl.fontys.s3.dinemasterbackend.business.item_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.update.items.*;

public interface UpdateItem {
    void updateItem(UpdatePizzaRequest request);

    void updateItem(UpdatePastaRequest request);

    void updateItem(UpdateAppetizerRequest request);

    void updateItem(UpdateBeverageRequest request);

    void updateItem(UpdateBurgerRequest request);

    void updateItem(UpdateGrillRequest request);

    void updateItem(UpdateSaladRequest request);

    void updateItem(UpdateSoupRequest request);


}
