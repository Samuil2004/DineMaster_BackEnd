package nl.fontys.s3.dinemasterbackend.controller;

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

    //As a CUSTOMER I can check what is inside my cart
    @RolesAllowed({"CUSTOMER"})
    @GetMapping("/carts-active/{customerId}")
    public ResponseEntity<GetCartResponse> getCartForCustomer(@PathVariable("customerId") @Min(1) Long customerId) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }

        GetCartRequest request = GetCartRequest.builder().customerId(customerId).build();
        GetCartResponse response = getCart.getCart(request, accessToken);
        return ResponseEntity.ok(response);
    }

    //As a CUSTOMER I can check if I have any active orders
    @RolesAllowed({"CUSTOMER"})
    @GetMapping("/orders/{customerId}/active")
    public ResponseEntity<CheckForCustomerActiveOrderResponse> checkForCustomerActiveOrder(@PathVariable("customerId") Long customerId) {
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }
        CheckForCustomerActiveOrderRequest request = CheckForCustomerActiveOrderRequest.builder().customerId(customerId).build();
        CheckForCustomerActiveOrderResponse response = checkActiveOrdersForCustomer.checkForCustomerActiveOrder(request, accessToken);
        return ResponseEntity.ok(response);
    }

    //As a CUSTOMER I can check if I have any active carts
    @RolesAllowed({"CUSTOMER"})
    @GetMapping("/cart/{customerId}/active")
    public ResponseEntity<CheckForCustomerActiveCartResponse> checkForCustomerActiveCart(@PathVariable("customerId") Long customerId) {
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }
        CheckForCustomerActiveCartRequest request = CheckForCustomerActiveCartRequest.builder().customerId(customerId).build();
        CheckForCustomerActiveCartResponse response = checkActiveCartForCustomer.checkActiveCart(request, accessToken);
        return ResponseEntity.ok(response);
    }

    //As a CUSTOMER I can place an order
    @RolesAllowed({"CUSTOMER"})
    @PostMapping("/cart/{customerId}/checkout")
    public ResponseEntity<CreateOrderResponse> createOrder(@PathVariable("customerId") Long customerId,
                                                           @RequestBody @Valid CreateOrderRequest createOrderRequest) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }

        CreateOrderResponse response2 = createOrder.createOrder(createOrderRequest, accessToken);
        return ResponseEntity.ok(response2);
    }

    //As a CUSTOMER only I can add items to my cart
    @RolesAllowed({"CUSTOMER"})
    @PostMapping("/carts/item")
    public ResponseEntity<Void> addItemToCart(
            @Valid @RequestBody AddItemDifferentFromPizzaToCartRequest request) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }

        addItemToCart.addItemToCart(request, accessToken);
        return ResponseEntity.noContent().build();
    }

    //As a CUSTOMER only I can add items to my cart
    @RolesAllowed({"CUSTOMER"})
    @PostMapping("/carts/pizza")
    public ResponseEntity<Void> addPizzaToCart(@Valid @RequestBody AddPizzaToCartRequest request) {
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }
        addItemToCart.addItemToCart(request, accessToken);
        return ResponseEntity.noContent().build();
    }

    //As a Delivery only I can only assignOrdersToMyself
    @RolesAllowed({"DELIVERY"})
    @PostMapping("/orders/assign")
    public ResponseEntity<Void> assignOrderToDeliveryPerson(@Valid @RequestBody AssignOrderToDeliveryPersonRequest request) {
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }

        assignOrderToDeliveryPerson.assignOrderToDeliveryPerson(request, accessToken);
        return ResponseEntity.noContent().build();
    }

    //As a COOK only I can get the orders by status
    @RolesAllowed({"COOK", "DELIVERY"})
    @GetMapping("/orders/status/{status}")
    public ResponseEntity<GetOrdersResponse> getAllOrders(@PathVariable("status") String status,
                                                          @RequestParam(value = "isTaken", required = false) Boolean isTaken) {

        GetOrderByStatusRequest request = GetOrderByStatusRequest.builder().status(status).isTaken(isTaken).build();
        GetOrdersResponse response = getOrdersByStatus.getOrdersByStatus(request);
        return ResponseEntity.ok(response);
    }

    //CHECK IF IT IS STILL NEEDED
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<GetOrderByIdResponse> getOrderById(@PathVariable("orderId") Long orderId) {

        GetOrderByIdRequest request = GetOrderByIdRequest.builder().orderId(orderId).build();
        GetOrderByIdResponse response = getOrderById.getOrderById(request);
        return ResponseEntity.ok(response);
    }

    @RolesAllowed({"DELIVERY", "MANAGER"})
    @GetMapping("/orders/delivery/{deliveryGuyId}")
    public ResponseEntity<GetOrdersForDeliveryPersonResponse> getOrdersForDeliveryForDeliveryGuy(@PathVariable("deliveryGuyId") Long deliveryGuyId,
                                                                                                 @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                                                                 @RequestParam(value = "statusId", required = true) Long statusId) {
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }
        GetOrdersForDeliveryPersonRequest request = GetOrdersForDeliveryPersonRequest.builder().deliveryPersonId(deliveryGuyId).pageNumber(pageNumber).statusId(statusId).build();
        GetOrdersForDeliveryPersonResponse response = getOrdersForDeliveryPerson.getActiveOrdersForDeliveryPerson(request, accessToken);
        return ResponseEntity.ok(response);
    }


    //As a COOK only I can get the selected items per category
    @RolesAllowed({"COOK"})
    @GetMapping("/selected-items")
    public ResponseEntity<GetSelectedItemsInOrdersByCategoryAndStatusResponse>
    getAllSelectedItemsByCategoryAndStatus(
            @RequestParam("category") String category,
            @RequestParam("status") String status
    ) {

        GetSelectedItemsInOrdersByCategoryAndStatusRequest request =
                GetSelectedItemsInOrdersByCategoryAndStatusRequest.builder()
                        .category(category)
                        .status(status)
                        .build();

        GetSelectedItemsInOrdersByCategoryAndStatusResponse response =
                getSelectedItemsInOrdersByCategoryAndStatus.getSelectedItemsInOrdersByCategoryAndStatus(request);

        return ResponseEntity.ok(response);
    }


    //As a COOK or DELIVERY only I can update the status of an order
    @RolesAllowed({"COOK", "DELIVERY"})
    @PutMapping("/orders/{orderId}/{status}")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable("orderId") Long orderId,
                                                  @PathVariable("status") String status) {
        UpdateOrderStatusRequest request = UpdateOrderStatusRequest.builder().orderId(orderId).status(status).build();
        updateOrderStatus.updateOrderStatus(request);
        return ResponseEntity.noContent().build();
    }

    //As a COOK only I can update the status of a selected item
    @RolesAllowed({"COOK"})
    @PutMapping("/selected-item/{selectedItemId}")
    public ResponseEntity<Void> updateSelectedItemStatus(@PathVariable("selectedItemId") Long selectedItemId) {
        UpdateSelectedItemStatusRequest request = UpdateSelectedItemStatusRequest.builder().selectedItemId(selectedItemId).build();
        updateSelectedItemStatus.updateSelectedItemStatus(request);
        return ResponseEntity.noContent().build();
    }

    //As a CUSTOMER only I can delete items from my cart
    @RolesAllowed({"CUSTOMER"})
    @DeleteMapping("/cart/{cartId}/items/{selectedItemId}")
    public ResponseEntity<Void> deleteItemFromCart(@PathVariable("cartId") @Min(value = 1, message = "Cart ID must be at least 1.") Long cartId,
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
