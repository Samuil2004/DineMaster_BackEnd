package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.CheckForCustomerActiveOrderRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.CheckForCustomerActiveOrderResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.implementations.AccessTokenImpl;
import nl.fontys.s3.dinemasterbackend.persistence.entity.CustomerEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CheckActiveOrdersForCustomerImplTest {
    @InjectMocks
    private CheckActiveOrdersForCustomerImpl checkActiveOrdersForCustomer;

    @Mock
    OrderEntityRepository orderEntityRepository;

    CheckForCustomerActiveOrderRequest checkForCustomerActiveOrderRequest;
    CheckForCustomerActiveOrderResponse checkForCustomerActiveOrderResponse;
    CheckForCustomerActiveOrderResponse checkForCustomerActiveOrderResponse2;

    OrderEntity orderEntity;
    AccessTokenImpl accessToken;
    AccessTokenImpl accessTokenFake;

    CustomerEntity customerEntity;

    @BeforeEach
    void setUp() {
        checkForCustomerActiveOrderRequest = CheckForCustomerActiveOrderRequest.builder().customerId(1L).build();
        checkForCustomerActiveOrderResponse = CheckForCustomerActiveOrderResponse.builder().thereAreActiveOrders(true).activeOrderId(1L).build();
        checkForCustomerActiveOrderResponse2 = CheckForCustomerActiveOrderResponse.builder().thereAreActiveOrders(false).build();

        customerEntity = CustomerEntity.builder().userId(1L).build();
        orderEntity = OrderEntity.builder()
                .orderId(1L)
                .cartEntity(null)
                .comments("No comments")
                .orderStatus(null)
                .customerEntity(customerEntity)
                .deliveryFee(5.00)
                .build();

        accessToken = new AccessTokenImpl("test@test", 1L, Set.of("CUSTOMER"));
        accessTokenFake = new AccessTokenImpl("fake@fake", 2L, Set.of("CUSTOMER"));

    }

    @Test
    void givenCustomerId_whenCheckingIfCustomerHasActiveOrder_returnsActiveOrderAndOrderId() {
        when(orderEntityRepository.findOrderByCustomerIdAndStatusNotDelivered(checkForCustomerActiveOrderRequest.getCustomerId())).thenReturn(Optional.ofNullable(orderEntity));

        CheckForCustomerActiveOrderResponse response = checkActiveOrdersForCustomer.checkForCustomerActiveOrder(checkForCustomerActiveOrderRequest, accessToken);

        assertEquals(response.getActiveOrderId(), checkForCustomerActiveOrderResponse.getActiveOrderId());
        assertEquals(response.getActiveOrderId(), checkForCustomerActiveOrderResponse.getActiveOrderId());

        verify(orderEntityRepository).findOrderByCustomerIdAndStatusNotDelivered(checkForCustomerActiveOrderRequest.getCustomerId());
    }

    @Test
    void givenCustomerId_whenCheckingIfCustomerHasActiveOrder_returnsFalseThereIsActiveOrder() {
        when(orderEntityRepository.findOrderByCustomerIdAndStatusNotDelivered(checkForCustomerActiveOrderRequest.getCustomerId())).thenReturn(Optional.empty());

        CheckForCustomerActiveOrderResponse response = checkActiveOrdersForCustomer.checkForCustomerActiveOrder(checkForCustomerActiveOrderRequest, accessToken);

        assertEquals(response.getActiveOrderId(), checkForCustomerActiveOrderResponse2.getActiveOrderId());

        verify(orderEntityRepository).findOrderByCustomerIdAndStatusNotDelivered(checkForCustomerActiveOrderRequest.getCustomerId());
    }

    @Test
    void givenCustomerId_whenCheckingIfCustomerHasActiveOrder_throwsErrorWhenAccessTokenBelongsToAnotherCustomer() {
        when(orderEntityRepository.findOrderByCustomerIdAndStatusNotDelivered(checkForCustomerActiveOrderRequest.getCustomerId())).thenReturn(Optional.ofNullable(orderEntity));

        AccessDenied exception = assertThrows(AccessDenied.class, () -> {
            checkActiveOrdersForCustomer.checkForCustomerActiveOrder(checkForCustomerActiveOrderRequest, accessTokenFake);
        });

        assertEquals("Customers have access only to their own orders.", exception.getReason());
        verify(orderEntityRepository).findOrderByCustomerIdAndStatusNotDelivered(checkForCustomerActiveOrderRequest.getCustomerId());
    }
}