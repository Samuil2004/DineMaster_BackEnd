package nl.fontys.s3.dinemasterbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.*;
import nl.fontys.s3.dinemasterbackend.business.dtos.delete.DeleteItemFromCartRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.*;
import nl.fontys.s3.dinemasterbackend.business.dtos.update.orders.UpdateOrderStatusRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.update.orders.UpdateSelectedItemStatusRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.Unauthorized;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.*;
import nl.fontys.s3.dinemasterbackend.configuration.security.auth.RequestAuthenticatedUserProvider;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class OrderingController {
    private final GetCart getCart;
    private final CreateOrder createOrder;
    private final AddItemToCart addItemToCart;
    private final GetOrdersByStatus getOrdersByStatus;
    private final UpdateOrderStatus updateOrderStatus;
    private final DeleteItemFromCart deleteItemFromCart;
    private final CheckActiveOrdersForCustomer checkActiveOrdersForCustomer;
    private final CheckActiveCartForCustomer checkActiveCartForCustomer;
    private final GetOrderById getOrderById;
    private RequestAuthenticatedUserProvider requestAuthenticatedUserProvider;
    private final AssignOrderToDeliveryPerson assignOrderToDeliveryPerson;
    private final GetOrdersForDeliveryPerson getOrdersForDeliveryPerson;

    private final GetSelectedItemsInOrdersByCategoryAndStatus getSelectedItemsInOrdersByCategoryAndStatus;
    private final UpdateSelectedItemStatus updateSelectedItemStatus;

    private static final String UNAUTHORIZED_MESSAGE = "User not authenticated";

    /**
     * Endpoint for customers to check their cart.
     *
     * @param customerId The ID of the customer whose cart is being requested.
     * @return The cart details for the specified customer.
     */
    @Operation(
            summary = "Get customer's active cart",
            description = "This endpoint allows customers to check what is inside their active cart.",
            tags = {"Order Management"}
    )
    @RolesAllowed({"CUSTOMER"})
    @GetMapping("/carts-active/{customerId}")
    public ResponseEntity<GetCartResponse> getCartForCustomer(
            @Parameter(description = "The customer ID whose cart is to be fetched")
            @PathVariable("customerId") @Min(1) Long customerId) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }

        GetCartRequest request = GetCartRequest.builder().customerId(customerId).build();
        GetCartResponse response = getCart.getCart(request, accessToken);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for customers to check if they have any active orders.
     *
     * @param customerId The ID of the customer whose active orders are being checked.
     * @return Details about the customer's active orders.
     */
    @Operation(
            summary = "Check customer's active orders",
            description = "This endpoint allows customers to check if they have any active orders.",
            tags = {"Order Management"}
    )
    @RolesAllowed({"CUSTOMER"})
    @GetMapping("/orders/{customerId}/active")
    public ResponseEntity<CheckForCustomerActiveOrderResponse> checkForCustomerActiveOrder(
            @Parameter(description = "The customer ID whose active orders are being checked")
            @PathVariable("customerId") Long customerId) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }
        CheckForCustomerActiveOrderRequest request = CheckForCustomerActiveOrderRequest.builder().customerId(customerId).build();
        CheckForCustomerActiveOrderResponse response = checkActiveOrdersForCustomer.checkForCustomerActiveOrder(request, accessToken);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for customers to check if they have any active carts.
     *
     * @param customerId The ID of the customer whose active cart is being checked.
     * @return Details about the customer's active cart.
     */
    @Operation(
            summary = "Check customer's active cart",
            description = "This endpoint allows customers to check if they have an active cart.",
            tags = {"Order Management"}
    )
    @RolesAllowed({"CUSTOMER"})
    @GetMapping("/cart/{customerId}/active")
    public ResponseEntity<CheckForCustomerActiveCartResponse> checkForCustomerActiveCart(
            @Parameter(description = "The customer ID whose active cart is being checked")
            @PathVariable("customerId") Long customerId) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }
        CheckForCustomerActiveCartRequest request = CheckForCustomerActiveCartRequest.builder().customerId(customerId).build();
        CheckForCustomerActiveCartResponse response = checkActiveCartForCustomer.checkActiveCart(request, accessToken);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for customers to place an order.
     *
     * @param customerId The ID of the customer placing the order.
     * @param createOrderRequest The details of the order to be created.
     * @return The response containing the created order details.
     */
    @Operation(
            summary = "Place an order (Customer only)",
            description = "This endpoint allows customers to place an order for items in their cart.",
            tags = {"Order Management"}
    )
    @RolesAllowed({"CUSTOMER"})
    @PostMapping("/cart/{customerId}/checkout")
    public ResponseEntity<CreateOrderResponse> createOrder(
            @Parameter(description = "The customer ID placing the order")
            @PathVariable("customerId") Long customerId,

            @Parameter(description = "The details of the order to be placed")
            @RequestBody @Valid CreateOrderRequest createOrderRequest) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }

        CreateOrderResponse response2 = createOrder.createOrder(createOrderRequest, accessToken);
        return ResponseEntity.ok(response2);
    }

    /**
     * Endpoint for customers to add items to their cart.
     *
     * @param request The details of the item being added to the cart.
     * @return A 204 No Content response indicating the item was added successfully.
     */
    @Operation(
            summary = "Add item to cart (Customer only)",
            description = "This endpoint allows customers to add an item to their cart.",
            tags = {"Order Management"}
    )
    @RolesAllowed({"CUSTOMER"})
    @PostMapping("/carts/item")
    public ResponseEntity<Void> addItemToCart(
            @Parameter(description = "The item details to add to the cart")
            @Valid @RequestBody AddItemDifferentFromPizzaToCartRequest request) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }

        addItemToCart.addItemToCart(request, accessToken);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for customers to add pizza to their cart.
     *
     * @param request The details of the pizza being added to the cart.
     * @return A 204 No Content response indicating the pizza was added successfully.
     */
    @Operation(
            summary = "Add pizza to cart (Customer only)",
            description = "This endpoint allows customers to add a pizza to their cart.",
            tags = {"Order Management"}
    )
    @RolesAllowed({"CUSTOMER"})
    @PostMapping("/carts/pizza")
    public ResponseEntity<Void> addPizzaToCart(
            @Parameter(description = "The pizza details to add to the cart")
            @Valid @RequestBody AddPizzaToCartRequest request) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }
        addItemToCart.addItemToCart(request, accessToken);
        return ResponseEntity.noContent().build();
    }


    /**
     * Endpoint for assigning an order to a delivery person (Delivery only).
     *
     * @param request The request containing the order and delivery person details.
     * @return A 204 No Content response indicating the order has been assigned.
     */
    @Operation(
            summary = "Assign order to delivery person (Delivery only)",
            description = "This endpoint allows a delivery person to be assigned to an order. Only users with the 'DELIVERY' role can assign orders.",
            tags = {"Order Management"}
    )
    @RolesAllowed({"DELIVERY"})
    @PostMapping("/orders/assign")
    public ResponseEntity<Void> assignOrderToDeliveryPerson(
            @Parameter(description = "The order and delivery person details")
            @Valid @RequestBody AssignOrderToDeliveryPersonRequest request) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }

        assignOrderToDeliveryPerson.assignOrderToDeliveryPerson(request, accessToken);
        return ResponseEntity.noContent().build();
    }


    /**
     * Endpoint for retrieving all orders by status (Cook or Delivery only).
     *
     * @param status The status of the orders to retrieve (e.g., 'PREPARING', 'DELIVERED').
     * @param isTaken Optional flag indicating whether to filter orders by their taken status.
     * @return A list of orders with the specified status.
     */
    @Operation(
            summary = "Get orders by status (Cook or Delivery only)",
            description = "This endpoint allows cooks and delivery personnel to retrieve orders based on their status. Requires role-based access control.",
            tags = {"Order Management"}
    )
    @RolesAllowed({"COOK", "DELIVERY"})
    @GetMapping("/orders/status/{status}")
    public ResponseEntity<GetOrdersResponse> getAllOrders(
            @Parameter(description = "The status of the orders to retrieve")
            @PathVariable("status") String status,

            @Parameter(description = "Flag to filter orders by whether they have been taken by a delivery person")
            @RequestParam(value = "isTaken", required = false) Boolean isTaken) {

        GetOrderByStatusRequest request = GetOrderByStatusRequest.builder()
                .status(status)
                .isTaken(isTaken)
                .build();
        GetOrdersResponse response = getOrdersByStatus.getOrdersByStatus(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for retrieving an order by its ID.
     *
     * @param orderId The ID of the order to retrieve.
     * @return The details of the specified order.
     */
    @Operation(
            summary = "Get order by ID",
            description = "This endpoint retrieves a specific order based on the provided order ID.",
            tags = {"Order Management"}
    )
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<GetOrderByIdResponse> getOrderById(
            @Parameter(description = "The ID of the order to retrieve")
            @PathVariable("orderId") Long orderId) {

        GetOrderByIdRequest request = GetOrderByIdRequest.builder().orderId(orderId).build();
        GetOrderByIdResponse response = getOrderById.getOrderById(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for retrieving all orders assigned to a specific delivery person (Delivery and Manager roles).
     *
     * @param deliveryGuyId The ID of the delivery person.
     * @param pageNumber Optional page number for pagination.
     * @param statusId The status ID of the orders to retrieve.
     * @return A list of orders assigned to the delivery person.
     */
    @Operation(
            summary = "Get orders for delivery person (Delivery and Manager only)",
            description = "This endpoint allows delivery personnel or managers to retrieve orders assigned to a specific delivery person.",
            tags = {"Order Management"}
    )
    @RolesAllowed({"DELIVERY", "MANAGER"})
    @GetMapping("/orders/delivery/{deliveryGuyId}")
    public ResponseEntity<GetOrdersForDeliveryPersonResponse> getOrdersForDeliveryForDeliveryGuy(
            @Parameter(description = "The ID of the delivery person to retrieve orders for")
            @PathVariable("deliveryGuyId") Long deliveryGuyId,

            @Parameter(description = "Optional page number for pagination")
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,

            @Parameter(description = "The status ID of the orders to retrieve")
            @RequestParam(value = "statusId", required = true) Long statusId) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }

        GetOrdersForDeliveryPersonRequest request = GetOrdersForDeliveryPersonRequest.builder()
                .deliveryPersonId(deliveryGuyId)
                .pageNumber(pageNumber)
                .statusId(statusId)
                .build();
        GetOrdersForDeliveryPersonResponse response = getOrdersForDeliveryPerson.getActiveOrdersForDeliveryPerson(request, accessToken);
        return ResponseEntity.ok(response);
    }


    /**
     * Endpoint for retrieving selected items per category and status (Cook only).
     *
     * @param category The category of items to retrieve.
     * @param status The status of the items to retrieve.
     * @return A list of selected items in orders by category and status.
     */
    @Operation(
            summary = "Get selected items by category and status (Cook only)",
            description = "This endpoint allows cooks to retrieve selected items from orders based on their category and status.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"COOK"})
    @GetMapping("/selected-items")
    public ResponseEntity<GetSelectedItemsInOrdersByCategoryAndStatusResponse> getAllSelectedItemsByCategoryAndStatus(
            @Parameter(description = "The category of the items to retrieve")
            @RequestParam("category") String category,

            @Parameter(description = "The status of the items to retrieve")
            @RequestParam("status") String status) {

        GetSelectedItemsInOrdersByCategoryAndStatusRequest request = GetSelectedItemsInOrdersByCategoryAndStatusRequest.builder()
                .category(category)
                .status(status)
                .build();
        GetSelectedItemsInOrdersByCategoryAndStatusResponse response = getSelectedItemsInOrdersByCategoryAndStatus.getSelectedItemsInOrdersByCategoryAndStatus(request);
        return ResponseEntity.ok(response);
    }



    /**
     * Endpoint for updating the status of an order (Cook or Delivery only).
     *
     * @param orderId The ID of the order to update.
     * @param status The new status of the order.
     * @return A 204 No Content response indicating the status has been updated.
     */
    @Operation(
            summary = "Update order status (Cook or Delivery only)",
            description = "This endpoint allows cooks or delivery personnel to update the status of an order.",
            tags = {"Order Management"}
    )
    @RolesAllowed({"COOK", "DELIVERY"})
    @PutMapping("/orders/{orderId}/{status}")
    public ResponseEntity<Void> updateOrderStatus(
            @Parameter(description = "The ID of the order to update")
            @PathVariable("orderId") Long orderId,

            @Parameter(description = "The new status of the order")
            @PathVariable("status") String status) {

        UpdateOrderStatusRequest request = UpdateOrderStatusRequest.builder().orderId(orderId).status(status).build();
        updateOrderStatus.updateOrderStatus(request);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for updating the status of a selected item (Cook only).
     *
     * @param selectedItemId The ID of the selected item to update.
     * @return A 204 No Content response indicating the selected item status has been updated.
     */
    @Operation(
            summary = "Update selected item status (Cook only)",
            description = "This endpoint allows cooks to update the status of a selected item in an order.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"COOK"})
    @PutMapping("/selected-item/{selectedItemId}")
    public ResponseEntity<Void> updateSelectedItemStatus(
            @Parameter(description = "The ID of the selected item to update")
            @PathVariable("selectedItemId") Long selectedItemId) {

        UpdateSelectedItemStatusRequest request = UpdateSelectedItemStatusRequest.builder().selectedItemId(selectedItemId).build();
        updateSelectedItemStatus.updateSelectedItemStatus(request);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for deleting an item from the customer's cart (Customer only).
     *
     * @param cartId The ID of the cart to delete the item from.
     * @param selectedItemId The ID of the selected item to delete from the cart.
     * @return A 204 No Content response indicating the item has been removed from the cart.
     */
    @Operation(
            summary = "Delete item from cart (Customer only)",
            description = "This endpoint allows customers to delete a selected item from their cart.",
            tags = {"Cart Management"}
    )
    @RolesAllowed({"CUSTOMER"})
    @DeleteMapping("/cart/{cartId}/items/{selectedItemId}")
    public ResponseEntity<Void> deleteItemFromCart(
            @Parameter(description = "The ID of the cart to delete the item from")
            @PathVariable("cartId") @Min(value = 1, message = "Cart ID must be at least 1.") Long cartId,

            @Parameter(description = "The ID of the selected item to delete from the cart")
            @PathVariable("selectedItemId") @Min(value = 1, message = "Selected Item ID must be at least 1.") Long selectedItemId) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }

        DeleteItemFromCartRequest request = DeleteItemFromCartRequest.builder().cartId(cartId).selectedItemId(selectedItemId).build();
        deleteItemFromCart.deleteItemFromCart(request, accessToken);
        return ResponseEntity.noContent().build();
    }


}
