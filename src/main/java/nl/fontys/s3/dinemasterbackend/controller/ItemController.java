package nl.fontys.s3.dinemasterbackend.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.items.*;
import nl.fontys.s3.dinemasterbackend.business.dtos.delete.DeleteItemRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.items.*;
import nl.fontys.s3.dinemasterbackend.business.dtos.update.items.*;
import nl.fontys.s3.dinemasterbackend.business.item_use_cases.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final GetItemsFromCategory getItemsFromCategory;
    private final GetItemById getItemById;
    private final DeleteItemById deleteItemById;
    private final CreateItem createItem;
    private final UpdateItem updateItem;


    //All users should have access to the items per category
    @GetMapping("/categories/{category}")
    public ResponseEntity<GetItemsForCategoryResponse> getItemsByCategory(@PathVariable("category") String category,
                                                                          @RequestParam(value = "visibleInMenu", required = false, defaultValue = "false") Boolean visibleInMenu) {
        GetItemsForCategoryRequest request = GetItemsForCategoryRequest.builder().category(category).isVisibleInMenu(visibleInMenu).build();
        GetItemsForCategoryResponse response = getItemsFromCategory.readItemsByCategory(request);
        return ResponseEntity.ok(response);
    }

    //All users should have access to see detailed information for each item
    @GetMapping("/{id}")
    public ResponseEntity<GetItemByIdResponse> getItemFromCategoryById(@PathVariable("id") Long itemId) {
        GetItemByIdRequest request = GetItemByIdRequest.builder().itemId(itemId).build();
        GetItemByIdResponse response = getItemById.getItemById(request);
        return ResponseEntity.ok(response);
    }

    @RolesAllowed({"MANAGER"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemById(
            @RequestParam(value = "category", required = true)
            @Pattern(regexp = "\\S+", message = "Last name must not be blank.") String category,
            @PathVariable("id") Long itemId) {
        // Call your delete function
        DeleteItemRequest request = DeleteItemRequest.builder().category(category).itemId(itemId).build();
        deleteItemById.deleteItemById(request);
        return ResponseEntity.noContent().build();
    }

    //As a MANAGER I can add new items to the menu
    @RolesAllowed({"MANAGER"})
    @PostMapping("/pizza")
    public ResponseEntity<CreateItemResponse> createPizza(@RequestBody @Valid CreatePizzaRequest pizzaRequest) {
        CreateItemResponse createItemResponse = createItem.createItem(pizzaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createItemResponse);
    }

    @RolesAllowed({"MANAGER"})
    @PostMapping("/appetizer")
    public ResponseEntity<CreateItemResponse> createAppetizer(@RequestBody @Valid CreateAppetizerRequest appetizerRequest) {
        CreateItemResponse createItemResponse = createItem.createItem(appetizerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createItemResponse);
    }

    @RolesAllowed({"MANAGER"})
    @PostMapping("/beverage")
    public ResponseEntity<CreateItemResponse> createBeverage(@RequestBody @Valid CreateBeverageRequest beverageRequest) {
        CreateItemResponse createItemResponse = createItem.createItem(beverageRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createItemResponse);
    }

    @RolesAllowed({"MANAGER"})
    @PostMapping("/burger")
    public ResponseEntity<CreateItemResponse> createBurger(@RequestBody @Valid CreateBurgerRequest burgerRequest) {
        CreateItemResponse createItemResponse = createItem.createItem(burgerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createItemResponse);
    }

    @RolesAllowed({"MANAGER"})
    @PostMapping("/grill")
    public ResponseEntity<CreateItemResponse> createGrill(@RequestBody @Valid CreateGrillRequest grillRequest) {
        CreateItemResponse createItemResponse = createItem.createItem(grillRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createItemResponse);
    }


    @RolesAllowed({"MANAGER"})
    @PostMapping("/pasta")
    public ResponseEntity<CreateItemResponse> createPasta(@RequestBody @Valid CreatePastaRequest pastaRequest) {
        CreateItemResponse createItemResponse = createItem.createItem(pastaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createItemResponse);
    }

    @RolesAllowed({"MANAGER"})
    @PostMapping("/salad")
    public ResponseEntity<CreateItemResponse> createSalad(@RequestBody @Valid CreateSaladRequest saladRequest) {
        CreateItemResponse createItemResponse = createItem.createItem(saladRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createItemResponse);
    }

    @RolesAllowed({"MANAGER"})
    @PostMapping("/soup")
    public ResponseEntity<CreateItemResponse> createSoup(@RequestBody @Valid CreateSoupRequest soupRequest) {
        CreateItemResponse createItemResponse = createItem.createItem(soupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createItemResponse);
    }

    //As a MANAGER I can update all items in the menu

    @RolesAllowed({"MANAGER"})
    @PutMapping("/pizza/{id}")
    public ResponseEntity<Void> updatePizza(@PathVariable("id") Long itemId,
                                            @RequestBody @Valid UpdatePizzaRequest updatePizzaRequest) {
        updatePizzaRequest.setItemId(itemId);
        updateItem.updateItem(updatePizzaRequest);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"MANAGER"})
    @PutMapping("/pasta/{id}")
    public ResponseEntity<Void> updatePasta(@PathVariable("id") Long itemId,
                                            @RequestBody @Valid UpdatePastaRequest updatePastaRequest) {
        updatePastaRequest.setItemId(itemId);
        updateItem.updateItem(updatePastaRequest);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"MANAGER"})
    @PutMapping("/appetizer/{id}")
    public ResponseEntity<Void> updateAppetizer(@PathVariable("id") Long itemId,
                                                @RequestBody @Valid UpdateAppetizerRequest updateAppetizerRequest) {
        updateAppetizerRequest.setItemId(itemId);
        updateItem.updateItem(updateAppetizerRequest);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"MANAGER"})
    @PutMapping("/beverage/{id}")
    public ResponseEntity<Void> updateBeverage(@PathVariable("id") Long itemId,
                                               @RequestBody @Valid UpdateBeverageRequest updateBeverageRequest) {
        updateBeverageRequest.setItemId(itemId);
        updateItem.updateItem(updateBeverageRequest);
        return ResponseEntity.noContent().build();
    }


    @RolesAllowed({"MANAGER"})
    @PutMapping("/burger/{id}")
    public ResponseEntity<Void> updateBurger(@PathVariable("id") Long itemId,
                                             @RequestBody @Valid UpdateBurgerRequest updateBurgerRequest) {
        updateBurgerRequest.setItemId(itemId);
        updateItem.updateItem(updateBurgerRequest);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"MANAGER"})
    @PutMapping("/grill/{id}")
    public ResponseEntity<Void> updateGrill(@PathVariable("id") Long itemId,
                                            @RequestBody @Valid UpdateGrillRequest updateGrillRequest) {
        updateGrillRequest.setItemId(itemId);
        updateItem.updateItem(updateGrillRequest);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"MANAGER"})
    @PutMapping("/salad/{id}")
    public ResponseEntity<Void> updateSalad(@PathVariable("id") Long itemId,
                                            @RequestBody @Valid UpdateSaladRequest updateSaladRequest) {
        updateSaladRequest.setItemId(itemId);
        updateItem.updateItem(updateSaladRequest);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"MANAGER"})
    @PutMapping("/soup/{id}")
    public ResponseEntity<Void> updateSoup(@PathVariable("id") Long itemId,
                                           @RequestBody @Valid UpdateSoupRequest updateSoupRequest) {
        updateSoupRequest.setItemId(itemId);
        updateItem.updateItem(updateSoupRequest);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"MANAGER"})
    @DeleteMapping("/{id}/orders")
    public ResponseEntity<Void> deleteItemFromOrders(@PathVariable("id") Long itemId
    ) {
        DeleteItemRequest request = DeleteItemRequest.builder().itemId(itemId).build();

        deleteItemById.deleteItemFromOrders(request);
        return ResponseEntity.noContent().build();
    }

}
