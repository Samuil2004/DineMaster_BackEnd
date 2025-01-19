package nl.fontys.s3.dinemasterbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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


    /**
     * Endpoint to retrieve items by category, filtered by visibility in the menu.
     *
     * @param category The category of the items to retrieve.
     * @param visibleInMenu Filter items by their visibility in the menu.
     * @return A list of items in the specified category.
     */
    @Operation(
            summary = "Get items by category",
            description = "This endpoint retrieves a list of items in a specific category. You can filter the items by their visibility in the menu.",
            tags = {"Item Management"}
    )
    @GetMapping("/categories/{category}")
    public ResponseEntity<GetItemsForCategoryResponse> getItemsByCategory(
            @Parameter(description = "The category of items to retrieve")
            @PathVariable("category") String category,

            @Parameter(description = "Filter items by visibility in the menu (default is false)")
            @RequestParam(value = "visibleInMenu", required = false, defaultValue = "false") Boolean visibleInMenu) {

        GetItemsForCategoryRequest request = GetItemsForCategoryRequest.builder()
                .category(category)
                .isVisibleInMenu(visibleInMenu)
                .build();

        GetItemsForCategoryResponse response = getItemsFromCategory.readItemsByCategory(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to retrieve detailed information for a specific item.
     *
     * @param itemId The ID of the item to retrieve.
     * @return The details of the specified item.
     */
    @Operation(
            summary = "Get item by ID",
            description = "This endpoint retrieves detailed information about an item using its ID.",
            tags = {"Item Management"}
    )
    @GetMapping("/{id}")
    public ResponseEntity<GetItemByIdResponse> getItemFromCategoryById(
            @Parameter(description = "The ID of the item to retrieve")
            @PathVariable("id") Long itemId) {

        GetItemByIdRequest request = GetItemByIdRequest.builder().itemId(itemId).build();
        GetItemByIdResponse response = getItemById.getItemById(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to delete an item by ID (Manager only).
     *
     * @param category The category of the item to delete.
     * @param itemId The ID of the item to delete.
     * @return A 204 No Content response indicating successful deletion.
     */
    @Operation(
            summary = "Delete item by ID (Manager only)",
            description = "This endpoint allows Managers to delete an item from the menu. The item is specified by its ID.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemById(
            @Parameter(description = "The category of the item to delete")
            @RequestParam(value = "category", required = true)
            @Pattern(regexp = "\\S+", message = "Category must not be blank.") String category,

            @Parameter(description = "The ID of the item to delete")
            @PathVariable("id") Long itemId) {

        DeleteItemRequest request = DeleteItemRequest.builder()
                .category(category)
                .itemId(itemId)
                .build();

        deleteItemById.deleteItemById(request);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for creating a new pizza item (Manager only).
     *
     * @param pizzaRequest The details of the pizza item to create.
     * @return The created pizza item response.
     */
    @Operation(
            summary = "Create a new pizza item (Manager only)",
            description = "This endpoint allows Managers to add new pizza items to the menu.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PostMapping("/pizza")
    public ResponseEntity<CreateItemResponse> createPizza(
            @Parameter(description = "The details of the pizza item to create")
            @RequestBody @Valid CreatePizzaRequest pizzaRequest) {

        CreateItemResponse createItemResponse = createItem.createItem(pizzaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createItemResponse);
    }

    /**
     * Endpoint for creating a new appetizer item (Manager only).
     *
     * @param appetizerRequest The details of the appetizer item to create.
     * @return The created appetizer item response.
     */
    @Operation(
            summary = "Create a new appetizer item (Manager only)",
            description = "This endpoint allows Managers to add new appetizer items to the menu.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PostMapping("/appetizer")
    public ResponseEntity<CreateItemResponse> createAppetizer(
            @Parameter(description = "The details of the appetizer item to create")
            @RequestBody @Valid CreateAppetizerRequest appetizerRequest) {

        CreateItemResponse createItemResponse = createItem.createItem(appetizerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createItemResponse);
    }


    /**
     * Endpoint for creating a new beverage item (Manager only).
     *
     * @param beverageRequest The details of the beverage item to create.
     * @return The created beverage item response.
     */
    @Operation(
            summary = "Create a new beverage item (Manager only)",
            description = "This endpoint allows Managers to add new beverage items to the menu.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PostMapping("/beverage")
    public ResponseEntity<CreateItemResponse> createBeverage(
            @Parameter(description = "The details of the beverage item to create")
            @RequestBody @Valid CreateBeverageRequest beverageRequest) {

        CreateItemResponse createItemResponse = createItem.createItem(beverageRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createItemResponse);
    }

    /**
     * Endpoint for creating a new burger item (Manager only).
     *
     * @param burgerRequest The details of the burger item to create.
     * @return The created burger item response.
     */
    @Operation(
            summary = "Create a new burger item (Manager only)",
            description = "This endpoint allows Managers to add new burger items to the menu.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PostMapping("/burger")
    public ResponseEntity<CreateItemResponse> createBurger(
            @Parameter(description = "The details of the burger item to create")
            @RequestBody @Valid CreateBurgerRequest burgerRequest) {

        CreateItemResponse createItemResponse = createItem.createItem(burgerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createItemResponse);
    }


    /**
     * Endpoint for creating a new grill item (Manager only).
     *
     * @param grillRequest The details of the grill item to create.
     * @return The created grill item response.
     */
    @Operation(
            summary = "Create a new grill item (Manager only)",
            description = "This endpoint allows Managers to add new grill items to the menu.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PostMapping("/grill")
    public ResponseEntity<CreateItemResponse> createGrill(
            @Parameter(description = "The details of the grill item to create")
            @RequestBody @Valid CreateGrillRequest grillRequest) {

        CreateItemResponse createItemResponse = createItem.createItem(grillRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createItemResponse);
    }


    /**
     * Endpoint for creating a new pasta item (Manager only).
     *
     * @param pastaRequest The details of the pasta item to create.
     * @return The created pasta item response.
     */
    @Operation(
            summary = "Create a new pasta item (Manager only)",
            description = "This endpoint allows Managers to add new pasta items to the menu.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PostMapping("/pasta")
    public ResponseEntity<CreateItemResponse> createPasta(
            @Parameter(description = "The details of the pasta item to create")
            @RequestBody @Valid CreatePastaRequest pastaRequest) {

        CreateItemResponse createItemResponse = createItem.createItem(pastaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createItemResponse);
    }


    /**
     * Endpoint for creating a new salad item (Manager only).
     *
     * @param saladRequest The details of the salad item to create.
     * @return The created salad item response.
     */
    @Operation(
            summary = "Create a new salad item (Manager only)",
            description = "This endpoint allows Managers to add new salad items to the menu.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PostMapping("/salad")
    public ResponseEntity<CreateItemResponse> createSalad(
            @Parameter(description = "The details of the salad item to create")
            @RequestBody @Valid CreateSaladRequest saladRequest) {

        CreateItemResponse createItemResponse = createItem.createItem(saladRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createItemResponse);
    }

    /**
     * Endpoint for creating a new soup item (Manager only).
     *
     * @param soupRequest The details of the soup item to create.
     * @return The created soup item response.
     */
    @Operation(
            summary = "Create a new soup item (Manager only)",
            description = "This endpoint allows Managers to add new soup items to the menu.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PostMapping("/soup")
    public ResponseEntity<CreateItemResponse> createSoup(
            @Parameter(description = "The details of the soup item to create")
            @RequestBody @Valid CreateSoupRequest soupRequest) {

        CreateItemResponse createItemResponse = createItem.createItem(soupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createItemResponse);
    }

    /**
     * Endpoint for updating a pizza item (Manager only).
     *
     * @param itemId The ID of the pizza item to update.
     * @param updatePizzaRequest The updated details of the pizza item.
     * @return A 204 No Content response indicating successful update.
     */
    @Operation(
            summary = "Update pizza item (Manager only)",
            description = "This endpoint allows Managers to update the details of an existing pizza item.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PutMapping("/pizza/{id}")
    public ResponseEntity<Void> updatePizza(
            @Parameter(description = "The ID of the pizza item to update")
            @PathVariable("id") Long itemId,

            @Parameter(description = "The updated pizza item details")
            @RequestBody @Valid UpdatePizzaRequest updatePizzaRequest) {

        updatePizzaRequest.setItemId(itemId);
        updateItem.updateItem(updatePizzaRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for updating a pasta item (Manager only).
     *
     * @param itemId The ID of the pasta item to update.
     * @param updatePastaRequest The updated details of the pasta item.
     * @return A 204 No Content response indicating successful update.
     */
    @Operation(
            summary = "Update pasta item (Manager only)",
            description = "This endpoint allows Managers to update the details of an existing pasta item.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PutMapping("/pasta/{id}")
    public ResponseEntity<Void> updatePasta(
            @Parameter(description = "The ID of the pasta item to update")
            @PathVariable("id") Long itemId,

            @Parameter(description = "The updated pasta item details")
            @RequestBody @Valid UpdatePastaRequest updatePastaRequest) {

        updatePastaRequest.setItemId(itemId);
        updateItem.updateItem(updatePastaRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for updating an appetizer item (Manager only).
     *
     * @param itemId The ID of the appetizer item to update.
     * @param updateAppetizerRequest The updated details of the appetizer item.
     * @return A 204 No Content response indicating successful update.
     */
    @Operation(
            summary = "Update appetizer item (Manager only)",
            description = "This endpoint allows Managers to update the details of an existing appetizer item.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PutMapping("/appetizer/{id}")
    public ResponseEntity<Void> updateAppetizer(
            @Parameter(description = "The ID of the appetizer item to update")
            @PathVariable("id") Long itemId,

            @Parameter(description = "The updated appetizer item details")
            @RequestBody @Valid UpdateAppetizerRequest updateAppetizerRequest) {

        updateAppetizerRequest.setItemId(itemId);
        updateItem.updateItem(updateAppetizerRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for updating a beverage item (Manager only).
     *
     * @param itemId The ID of the beverage item to update.
     * @param updateBeverageRequest The updated details of the beverage item.
     * @return A 204 No Content response indicating successful update.
     */
    @Operation(
            summary = "Update beverage item (Manager only)",
            description = "This endpoint allows Managers to update the details of an existing beverage item.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PutMapping("/beverage/{id}")
    public ResponseEntity<Void> updateBeverage(
            @Parameter(description = "The ID of the beverage item to update")
            @PathVariable("id") Long itemId,

            @Parameter(description = "The updated beverage item details")
            @RequestBody @Valid UpdateBeverageRequest updateBeverageRequest) {

        updateBeverageRequest.setItemId(itemId);
        updateItem.updateItem(updateBeverageRequest);
        return ResponseEntity.noContent().build();
    }


    /**
     * Endpoint for updating a burger item (Manager only).
     *
     * @param itemId The ID of the burger item to update.
     * @param updateBurgerRequest The updated details of the burger item.
     * @return A 204 No Content response indicating successful update.
     */
    @Operation(
            summary = "Update burger item (Manager only)",
            description = "This endpoint allows Managers to update the details of an existing burger item.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PutMapping("/burger/{id}")
    public ResponseEntity<Void> updateBurger(
            @Parameter(description = "The ID of the burger item to update")
            @PathVariable("id") Long itemId,

            @Parameter(description = "The updated burger item details")
            @RequestBody @Valid UpdateBurgerRequest updateBurgerRequest) {

        updateBurgerRequest.setItemId(itemId);
        updateItem.updateItem(updateBurgerRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for updating a grill item (Manager only).
     *
     * @param itemId The ID of the grill item to update.
     * @param updateGrillRequest The updated details of the grill item.
     * @return A 204 No Content response indicating successful update.
     */
    @Operation(
            summary = "Update grill item (Manager only)",
            description = "This endpoint allows Managers to update the details of an existing grill item.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PutMapping("/grill/{id}")
    public ResponseEntity<Void> updateGrill(
            @Parameter(description = "The ID of the grill item to update")
            @PathVariable("id") Long itemId,

            @Parameter(description = "The updated grill item details")
            @RequestBody @Valid UpdateGrillRequest updateGrillRequest) {

        updateGrillRequest.setItemId(itemId);
        updateItem.updateItem(updateGrillRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for updating a salad item (Manager only).
     *
     * @param itemId The ID of the salad item to update.
     * @param updateSaladRequest The updated details of the salad item.
     * @return A 204 No Content response indicating successful update.
     */
    @Operation(
            summary = "Update salad item (Manager only)",
            description = "This endpoint allows Managers to update the details of an existing salad item.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PutMapping("/salad/{id}")
    public ResponseEntity<Void> updateSalad(
            @Parameter(description = "The ID of the salad item to update")
            @PathVariable("id") Long itemId,

            @Parameter(description = "The updated salad item details")
            @RequestBody @Valid UpdateSaladRequest updateSaladRequest) {

        updateSaladRequest.setItemId(itemId);
        updateItem.updateItem(updateSaladRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for updating a soup item (Manager only).
     *
     * @param itemId The ID of the soup item to update.
     * @param updateSoupRequest The updated details of the soup item.
     * @return A 204 No Content response indicating successful update.
     */
    @Operation(
            summary = "Update soup item (Manager only)",
            description = "This endpoint allows Managers to update the details of an existing soup item.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PutMapping("/soup/{id}")
    public ResponseEntity<Void> updateSoup(
            @Parameter(description = "The ID of the soup item to update")
            @PathVariable("id") Long itemId,

            @Parameter(description = "The updated soup item details")
            @RequestBody @Valid UpdateSoupRequest updateSoupRequest) {

        updateSoupRequest.setItemId(itemId);
        updateItem.updateItem(updateSoupRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for deleting an item from orders (Manager only).
     *
     * @param itemId The ID of the item to delete from orders.
     * @return A 204 No Content response indicating successful deletion.
     */
    @Operation(
            summary = "Delete item from all orders (Manager only)",
            description = "This endpoint allows Managers to delete an item from the all orders list.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @DeleteMapping("/{id}/orders")
    public ResponseEntity<Void> deleteItemFromOrders(
            @Parameter(description = "The ID of the item to delete from orders")
            @PathVariable("id") Long itemId) {

        DeleteItemRequest request = DeleteItemRequest.builder().itemId(itemId).build();
        deleteItemById.deleteItemFromOrders(request);
        return ResponseEntity.noContent().build();
    }

}
