package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import nl.fontys.s3.dinemasterpro.business.converters.CartConverter;
import nl.fontys.s3.dinemasterpro.business.converters.OrderConverter;
import nl.fontys.s3.dinemasterpro.business.converters.OrderStatusConverter;
import nl.fontys.s3.dinemasterpro.business.dtos.create.orders.CreateOrderResponse;
import nl.fontys.s3.dinemasterpro.business.dtos.get.ValidateAddressRequest;
import nl.fontys.s3.dinemasterpro.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterpro.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterpro.business.exceptions.OperationNotPossible;
import nl.fontys.s3.dinemasterpro.business.order_use_cases.CalculateDeliveryFee;
import nl.fontys.s3.dinemasterpro.business.user_use_cases.GetUserById;
import nl.fontys.s3.dinemasterpro.configuration.security.token.implementations.AccessTokenImpl;
import nl.fontys.s3.dinemasterpro.domain.classes.*;
import nl.fontys.s3.dinemasterpro.persistence.entity.*;
import nl.fontys.s3.dinemasterpro.business.dtos.create.orders.CreateOrderRequest;
import nl.fontys.s3.dinemasterpro.business.dtos.get.CalculateDeliveryFeeResponse;
import nl.fontys.s3.dinemasterpro.business.user_use_cases.UpdateUserOrderDetails;
import nl.fontys.s3.dinemasterpro.persistence.repositories.CartEntityRepository;
import nl.fontys.s3.dinemasterpro.persistence.repositories.OrderEntityRepository;
import nl.fontys.s3.dinemasterpro.persistence.repositories.OrderStatusEntityRepository;
import nl.fontys.s3.dinemasterpro.persistence.repositories.SelectedItemStatusOfPreparationEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CreateOrderImplTest {
    @InjectMocks
    private CreateOrderImpl createOrderImpl;

    @Mock
    private OrderEntityRepository orderEntityRepository;
    @Mock
    private GetUserById getUserById;
    @Mock
    private CartEntityRepository cartEntityRepository;
    @Mock
    private OrderStatusEntityRepository orderStatusEntityRepository;
    @Mock
    private SelectedItemStatusOfPreparationEntityRepository selectedItemStatusOfPreparationEntityRepository;
    @Mock
    private CalculateDeliveryFee calculateDeliveryFee;
    @Mock
    private OrderConverter orderConverter;
    @Mock
    private CartConverter cartConverter;
    @Mock
    private OrderStatusConverter orderStatusConverter;
    @Mock
    private UpdateUserOrderDetails updateUserOrderDetails;

    CreateOrderRequest createOrderRequest;
    Customer customer;
    AccessTokenImpl accessToken;
    CartEntity cartEntity;
    Cart cartBase;
    CartEntity cartEntityDifferentCartIdFromRequest;


    Order order;
    OrderEntity orderEntity;
    CalculateDeliveryFeeResponse deliveryFeeResponse;
    OrderStatusEntity orderStatusEntity;
    OrderStatus orderStatusBase;
    SelectedItemEntity selectedItemEntityPreparing;

    Address address;

    SelectedItemStatusOfPreparationEntity selectedItemStatusEntity;

    @BeforeEach
    void setUp() {
        createOrderRequest = CreateOrderRequest.builder()
                .customerId(1L)
                .cartId(1L)
                .comments("No comments")
                .phoneNumber("+319898765")
                .country("Netherlands")
                .city("city")
                .postalCode("3642DD")
                .street("street")
                .build();

        address = Address.builder()
                .country("Netherlands")
                .city("city")
                .street("street")
                .postalCode("3642DD")
                .build();

        customer = Customer.builder()
                .userId(1L)
                .phoneNumber("+319898765")
                .address(address)
                .firstName("FN")
                .lastName("LN")
                .email("test@test.com")
                .build();

        selectedItemStatusEntity = new SelectedItemStatusOfPreparationEntity();
        selectedItemStatusEntity.setId(1L);
        selectedItemStatusEntity.setStatusName("PREPARING");


        accessToken = new AccessTokenImpl("customer@test.com", 1L, Set.of("CUSTOMER"));

        cartEntity = CartEntity.builder()
                .cartId(1L)
                .customerId(1L)
                .price(50.0)
                .selectedItemEntities(new ArrayList<>())
                .isActive(true)
                .build();

        cartEntityDifferentCartIdFromRequest = CartEntity.builder()
                .cartId(2L)
                .customerId(1L)
                .price(50.0)
                .selectedItemEntities(new ArrayList<>())
                .isActive(true)
                .build();

        selectedItemEntityPreparing = SelectedItemEntity.builder()
                .selectedItemId(1L)
                .itemOfReference(null)
                .amount(1)
                .cart(cartEntity)
                .itemCategory(null)
                .comment("no comments")
                .statusOfPreparation(selectedItemStatusEntity)
                .build();

        cartEntity.setSelectedItemEntities(List.of(selectedItemEntityPreparing));


        cartBase = Cart.builder()
                .cartId(1L)
                .customerId(1L)
                .price(50.0)
                .selectedItems(new ArrayList<>())
                .isActive(true)
                .build();

        order = Order.builder()
                .cart(Cart.builder()
                        .cartId(1L)
                        .customerId(1L)
                        .price(50.0)
                        .selectedItems(new ArrayList<>())
                        .isActive(false)
                        .build())
                .comments("No comments")
                .customerWhoOrdered(customer)
                .deliveryFee(10.0)
                .orderStatus(OrderStatus.builder()
                        .id(1L)
                        .statusName("PREPARING")
                        .build())
                .build();

        orderEntity = OrderEntity.builder().orderId(1L).build();

        deliveryFeeResponse = CalculateDeliveryFeeResponse.builder().deliveryFee(10.0).build();

        orderStatusEntity = OrderStatusEntity.builder().id(1L).statusName("PREPARING").build();

        orderStatusBase = OrderStatus.builder().id(1L).statusName("PREPARING").build();


    }


    @Test
    void givenDifferentIdsInTheRequestAndInTheToken_whenCreatingOrder_thenThrowAccessDenied() {
        createOrderRequest.setCustomerId(2L);

        AccessDenied exception = assertThrows(AccessDenied.class, () -> {
            createOrderImpl.createOrder(createOrderRequest, accessToken);
        });
        assertEquals("Customers can only create orders for themselves.", exception.getReason());

    }

    @Test
    void givenNonExistingUserId_whenCreatingOrder_thenThrowNotFound() {
        when(getUserById.getUserByIdService(createOrderRequest.getCustomerId())).thenReturn(null);

        NotFound exception = assertThrows(NotFound.class, () -> {
            createOrderImpl.createOrder(createOrderRequest, accessToken);
        });
        assertEquals("USER NOT FOUND", exception.getReason());
        verify(getUserById).getUserByIdService(createOrderRequest.getCustomerId());
    }

    @Test
    void givenCustomerWithNonExistingActiveCart_whenCreatingOrder_thenThrowNotFound() {
        when(getUserById.getUserByIdService(createOrderRequest.getCustomerId())).thenReturn(customer);
        when(calculateDeliveryFee.calculateDeliveryFee(any(ValidateAddressRequest.class))).thenReturn(deliveryFeeResponse);
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(customer.getUserId()))
                .thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> {
            createOrderImpl.createOrder(createOrderRequest, accessToken);
        });
        assertEquals("CART NOT FOUND", exception.getReason());

        verify(getUserById).getUserByIdService(createOrderRequest.getCustomerId());
        verify(calculateDeliveryFee).calculateDeliveryFee(any(ValidateAddressRequest.class));
        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(customer.getUserId());
    }

    @Test
    void givenCartIdInRequestDoesNotExist_whenCreatingOrder_thenThrowNotFound() {
        when(getUserById.getUserByIdService(createOrderRequest.getCustomerId())).thenReturn(customer);
        when(calculateDeliveryFee.calculateDeliveryFee(any(ValidateAddressRequest.class))).thenReturn(deliveryFeeResponse);
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(customer.getUserId()))
                .thenReturn(Optional.ofNullable(cartEntity));
        when(cartEntityRepository.findById(createOrderRequest.getCartId())).thenReturn(Optional.ofNullable(cartEntityDifferentCartIdFromRequest));

        NotFound exception = assertThrows(NotFound.class, () -> {
            createOrderImpl.createOrder(createOrderRequest, accessToken);
        });
        assertEquals("CART NOT FOUND", exception.getReason());

        verify(getUserById).getUserByIdService(createOrderRequest.getCustomerId());
        verify(calculateDeliveryFee).calculateDeliveryFee(any(ValidateAddressRequest.class));
        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(customer.getUserId());
        verify(cartEntityRepository).findById(createOrderRequest.getCartId());
    }

    @Test
    void givenCartIdInRequestIsDifferentFromTheCartOwnedByTheCustomerId_whenCreatingOrder_thenThrowNotFound() {
        when(getUserById.getUserByIdService(createOrderRequest.getCustomerId())).thenReturn(customer);
        when(calculateDeliveryFee.calculateDeliveryFee(any(ValidateAddressRequest.class))).thenReturn(deliveryFeeResponse);
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(customer.getUserId()))
                .thenReturn(Optional.ofNullable(cartEntity));
        when(cartEntityRepository.findById(createOrderRequest.getCartId())).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> {
            createOrderImpl.createOrder(createOrderRequest, accessToken);
        });
        assertEquals("CART NOT FOUND", exception.getReason());

        verify(getUserById).getUserByIdService(createOrderRequest.getCustomerId());
        verify(calculateDeliveryFee).calculateDeliveryFee(any(ValidateAddressRequest.class));
        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(customer.getUserId());
        verify(cartEntityRepository).findById(createOrderRequest.getCartId());
    }


    @Test
    void givenWrongItemsWithinOrderStatus_whenCreatingOrder_thenThrowOperationNotPossible() {
        when(calculateDeliveryFee.calculateDeliveryFee(any(ValidateAddressRequest.class))).thenReturn(deliveryFeeResponse);
        when(getUserById.getUserByIdService(createOrderRequest.getCustomerId())).thenReturn(customer);
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(customer.getUserId()))
                .thenReturn(Optional.of(cartEntity));
        when(cartEntityRepository.findById(createOrderRequest.getCartId()))
                .thenReturn(Optional.of(cartEntity));
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName("PREPARING"))
                .thenReturn(Optional.empty());

        OperationNotPossible exception = assertThrows(OperationNotPossible.class, () -> {
            createOrderImpl.createOrder(createOrderRequest, accessToken);
        });
        assertEquals("SELECTED STATUS NOT FOUND", exception.getReason());
        verify(calculateDeliveryFee).calculateDeliveryFee(any(ValidateAddressRequest.class));
        verify(getUserById).getUserByIdService(createOrderRequest.getCustomerId());
        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(customer.getUserId());
        verify(cartEntityRepository).findById(createOrderRequest.getCartId());
        verify(selectedItemStatusOfPreparationEntityRepository).findByStatusName("PREPARING");

    }

    @Test
    void givenWrongOrderStatus_whenCreatingOrder_thenThrowOperationNotPossible() {
        when(calculateDeliveryFee.calculateDeliveryFee(any())).thenReturn(deliveryFeeResponse);
        when(getUserById.getUserByIdService(createOrderRequest.getCustomerId())).thenReturn(customer);
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(customer.getUserId()))
                .thenReturn(Optional.of(cartEntity));
        when(cartEntityRepository.findById(createOrderRequest.getCartId()))
                .thenReturn(Optional.of(cartEntity));
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName("PREPARING"))
                .thenReturn(Optional.of(selectedItemStatusEntity));
        when(cartEntityRepository.save(cartEntity)).thenReturn(cartEntity);
        when(orderStatusEntityRepository.findByStatusName("PREPARING"))
                .thenReturn(Optional.empty());

        OperationNotPossible exception = assertThrows(OperationNotPossible.class, () -> {
            createOrderImpl.createOrder(createOrderRequest, accessToken);
        });
        assertEquals("SELECTED STATUS NOT FOUND", exception.getReason());

        verify(calculateDeliveryFee).calculateDeliveryFee(any(ValidateAddressRequest.class));
        verify(getUserById).getUserByIdService(createOrderRequest.getCustomerId());
        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(customer.getUserId());
        verify(cartEntityRepository).findById(createOrderRequest.getCartId());
        verify(selectedItemStatusOfPreparationEntityRepository).findByStatusName("PREPARING");
        verify(cartEntityRepository).save(cartEntity);
        verify(orderStatusEntityRepository).findByStatusName("PREPARING");
    }

    @Test
    void givenRequestToCreateOrder_whenCreatingOrder_thenCreatesOrderAndReturnsCreatedOrder() {
        when(calculateDeliveryFee.calculateDeliveryFee(any())).thenReturn(deliveryFeeResponse);
        when(getUserById.getUserByIdService(createOrderRequest.getCustomerId())).thenReturn(customer);
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(customer.getUserId()))
                .thenReturn(Optional.of(cartEntity));
        when(cartEntityRepository.findById(createOrderRequest.getCartId()))
                .thenReturn(Optional.of(cartEntity));
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName("PREPARING"))
                .thenReturn(Optional.of(selectedItemStatusEntity));
        when(cartEntityRepository.save(cartEntity)).thenReturn(cartEntity);
        when(orderStatusEntityRepository.findByStatusName("PREPARING"))
                .thenReturn(Optional.ofNullable(orderStatusEntity));
        when(cartConverter.convertEntityToNormal(cartEntity)).thenReturn(cartBase);
        when(orderStatusConverter.convertEntityToNormal(orderStatusEntity)).thenReturn(orderStatusBase);
        when(orderConverter.convertNormalToEntity(any(Order.class))).thenReturn(orderEntity);
        when(orderEntityRepository.save(orderEntity)).thenReturn(orderEntity);
        when(orderConverter.convertEntityToNormal(orderEntity)).thenReturn(order);

        CreateOrderResponse response = createOrderImpl.createOrder(createOrderRequest, accessToken);
        assertEquals(response.getOrder(), order);

        verify(calculateDeliveryFee).calculateDeliveryFee(any(ValidateAddressRequest.class));
        verify(getUserById).getUserByIdService(createOrderRequest.getCustomerId());
        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(customer.getUserId());
        verify(cartEntityRepository).findById(createOrderRequest.getCartId());
        verify(selectedItemStatusOfPreparationEntityRepository).findByStatusName("PREPARING");
        verify(cartEntityRepository).save(cartEntity);
        verify(orderStatusEntityRepository).findByStatusName("PREPARING");
        verify(cartConverter).convertEntityToNormal(cartEntity);
        verify(orderStatusConverter).convertEntityToNormal(orderStatusEntity);
        verify(orderConverter).convertEntityToNormal(orderEntity);
        verify(orderEntityRepository).save(orderEntity);
        verify(orderConverter).convertNormalToEntity(any(Order.class));

    }


}