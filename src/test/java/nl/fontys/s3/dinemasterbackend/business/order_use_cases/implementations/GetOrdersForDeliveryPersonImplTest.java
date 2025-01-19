package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import nl.fontys.s3.dinemasterbackend.business.converters.OrderConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.GetOrdersForDeliveryPersonResponse;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrdersForDeliveryPersonRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.GetUserById;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.implementations.AccessTokenImpl;
import nl.fontys.s3.dinemasterbackend.domain.classes.Order;
import nl.fontys.s3.dinemasterbackend.domain.classes.User;
import nl.fontys.s3.dinemasterbackend.domain.enumerations.UserRole;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderDeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetOrdersForDeliveryPersonImplTest {
    @InjectMocks
    private GetOrdersForDeliveryPersonImpl getOrdersForDeliveryPerson;

    @Mock
    private OrderDeliveryRepository orderDeliveryRepository;

    @Mock
    private OrderConverter orderConverter;

    @Mock
    private GetUserById getUserById;

    private GetOrdersForDeliveryPersonRequest request;
    private GetOrdersForDeliveryPersonRequest requestWithNoPageNumber;

    private AccessToken accessToken;
    private AccessToken accessTokenManager;
    private User deliveryUser;
    private OrderEntity orderEntity;
    private Order order;
    private Pageable pageableFirstPage;
    private long totalCount;


    @BeforeEach
    void setUp() {
        request = GetOrdersForDeliveryPersonRequest.builder()
                .deliveryPersonId(1L)
                .pageNumber(1)
                .statusId(3L)
                .build();


        requestWithNoPageNumber = GetOrdersForDeliveryPersonRequest.builder()
                .deliveryPersonId(1L)
                .statusId(3L)
                .build();

        Set<String> roles = Set.of("DELIVERY");
        accessToken = new AccessTokenImpl("delivery@test.com", 1L, roles);

        Set<String> roles2 = Set.of("MANAGER");
        accessTokenManager = new AccessTokenImpl("manager@test.com", 1L, roles2);

        deliveryUser = User.builder()
                .userId(1L)
                .userRole(UserRole.DELIVERY)
                .build();

        orderEntity = OrderEntity.builder()
                .orderId(100L)
                .build();

        order = Order.builder()
                .orderId(100L)
                .build();
        pageableFirstPage = PageRequest.of(0, 10);
        totalCount = 1L;

    }

    @Test
    void givenDeliveryRoleWithDifferentUserId_whenGetActiveOrders_thenThrowAccessDenied() {
        accessToken = new AccessTokenImpl("delivery@test.com", 2L, Set.of("DELIVERY")); // Different userId

        AccessDenied exception = assertThrows(AccessDenied.class, () ->
                getOrdersForDeliveryPerson.getActiveOrdersForDeliveryPerson(request, accessToken)
        );

        assertEquals("Delivery personnel can only view their own orders.", exception.getReason());
    }

    @Test
    void givenNonDeliveryRole_whenGetActiveOrders_thenThrowNotFound() {
        when(getUserById.getUserByIdService(request.getDeliveryPersonId()))
                .thenReturn(User.builder().userRole(UserRole.CUSTOMER).build());

        NotFound exception = assertThrows(NotFound.class, () ->
                getOrdersForDeliveryPerson.getActiveOrdersForDeliveryPerson(request, accessToken)
        );

        assertEquals("Delivery person with provided person was not found", exception.getReason());
        verify(getUserById).getUserByIdService(request.getDeliveryPersonId());
    }

    @Test
    void givenNoActiveOrders_whenGetActiveOrders_thenThrowNotFound() {
        when(getUserById.getUserByIdService(request.getDeliveryPersonId()))
                .thenReturn(deliveryUser);
        when(orderDeliveryRepository.findOrdersForDeliveryPersonWithStatusForDeliveryPaginated(1L, 3L, pageableFirstPage))
                .thenReturn(List.of());

        NotFound exception = assertThrows(NotFound.class, () ->
                getOrdersForDeliveryPerson.getActiveOrdersForDeliveryPerson(request, accessToken)
        );

        assertEquals("No orders for delivery were found", exception.getReason());
        verify(getUserById).getUserByIdService(request.getDeliveryPersonId());
        verify(orderDeliveryRepository).findOrdersForDeliveryPersonWithStatusForDeliveryPaginated(1L, 3L, pageableFirstPage);
    }

    @Test
    void givenDeliveryGuyLookingForOrders_whenGetActiveOrders_thenReturnOrders() {
        when(getUserById.getUserByIdService(request.getDeliveryPersonId()))
                .thenReturn(deliveryUser);
        when(orderDeliveryRepository.findOrdersForDeliveryPersonWithStatusForDeliveryPaginated(1L, 3L, pageableFirstPage))
                .thenReturn(List.of(orderEntity));
        when(orderConverter.convertEntityToNormal(orderEntity))
                .thenReturn(order);
        when(orderDeliveryRepository.countTotalOrdersForDeliveryPerson(1L, 3L)).thenReturn(totalCount);
        GetOrdersForDeliveryPersonResponse response =
                getOrdersForDeliveryPerson.getActiveOrdersForDeliveryPerson(request, accessToken);

        assertNotNull(response);
        assertEquals(1, response.getAllOrdersAssignedToToADeliveryPerson().size());
        assertEquals(order, response.getAllOrdersAssignedToToADeliveryPerson().get(0));

        verify(getUserById).getUserByIdService(request.getDeliveryPersonId());
        verify(orderDeliveryRepository).findOrdersForDeliveryPersonWithStatusForDeliveryPaginated(1L, 3L, pageableFirstPage);
        verify(orderConverter).convertEntityToNormal(orderEntity);
    }

    @Test
    void givenManagerLookingForOrders_whenGetActiveOrders_thenReturnOrders() {
        when(getUserById.getUserByIdService(request.getDeliveryPersonId()))
                .thenReturn(deliveryUser);
        when(orderDeliveryRepository.findOrdersForDeliveryPersonWithStatusForDeliveryPaginated(1L, 3L, pageableFirstPage))
                .thenReturn(List.of(orderEntity));
        when(orderConverter.convertEntityToNormal(orderEntity))
                .thenReturn(order);
        when(orderDeliveryRepository.countTotalOrdersForDeliveryPerson(1L, 3L)).thenReturn(totalCount);
        GetOrdersForDeliveryPersonResponse response =
                getOrdersForDeliveryPerson.getActiveOrdersForDeliveryPerson(request, accessTokenManager);

        assertNotNull(response);
        assertEquals(1, response.getAllOrdersAssignedToToADeliveryPerson().size());
        assertEquals(order, response.getAllOrdersAssignedToToADeliveryPerson().get(0));

        verify(getUserById).getUserByIdService(request.getDeliveryPersonId());
        verify(orderDeliveryRepository).findOrdersForDeliveryPersonWithStatusForDeliveryPaginated(1L, 3L, pageableFirstPage);
        verify(orderConverter).convertEntityToNormal(orderEntity);
    }

    @Test
    void givenManagerLookingForOrdersWhenNoPageIsProvided_whenGetActiveOrders_thenReturnOrders() {
        when(getUserById.getUserByIdService(request.getDeliveryPersonId()))
                .thenReturn(deliveryUser);
        when(orderDeliveryRepository.findOrdersForDeliveryPersonWithStatusForDeliveryPaginated(1L, 3L, pageableFirstPage))
                .thenReturn(List.of(orderEntity));
        when(orderConverter.convertEntityToNormal(orderEntity))
                .thenReturn(order);
        when(orderDeliveryRepository.countTotalOrdersForDeliveryPerson(1L, 3L)).thenReturn(totalCount);
        GetOrdersForDeliveryPersonResponse response =
                getOrdersForDeliveryPerson.getActiveOrdersForDeliveryPerson(requestWithNoPageNumber, accessTokenManager);

        assertNotNull(response);
        assertEquals(1, response.getAllOrdersAssignedToToADeliveryPerson().size());
        assertEquals(order, response.getAllOrdersAssignedToToADeliveryPerson().get(0));

        verify(getUserById).getUserByIdService(request.getDeliveryPersonId());
        verify(orderDeliveryRepository).findOrdersForDeliveryPersonWithStatusForDeliveryPaginated(1L, 3L, pageableFirstPage);
        verify(orderConverter).convertEntityToNormal(orderEntity);
    }

}