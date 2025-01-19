package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import nl.fontys.s3.dinemasterbackend.business.converters.OrderConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrderByStatusRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrdersResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.domain.classes.Order;
import nl.fontys.s3.dinemasterbackend.domain.classes.OrderStatus;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderStatusEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderStatusEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOrdersByStatusImplTest {
    @InjectMocks
    private GetOrdersByStatusImpl getOrdersByStatusImpl;

    @Mock
    private OrderEntityRepository orderEntityRepository;

    @Mock
    private OrderStatusEntityRepository orderStatusEntityRepository;

    @Mock
    private OrderConverter orderConverter;

    private GetOrderByStatusRequest request;
    private GetOrderByStatusRequest requestIsTaken;

    private OrderStatusEntity orderStatus;
    private OrderStatus orderStatusBase;

    private OrderEntity orderEntity;
    private Order orderBase;

    @BeforeEach
    void setUp() {
        request = GetOrderByStatusRequest.builder().status("PREPARING").build();
        requestIsTaken = GetOrderByStatusRequest.builder().status("PREPARING").isTaken(true).build();

        orderStatus = OrderStatusEntity.builder().statusName("PREPARING").build();
        orderStatusBase = OrderStatus.builder().statusName("PREPARING").build();

        orderEntity = OrderEntity.builder().orderId(1L).orderStatus(orderStatus).build();

        orderBase = Order.builder().orderId(1L).orderStatus(orderStatusBase).build();
    }

    @Test
    void givenValidStatus_whenOrdersFound_thenReturnOrders() {
        when(orderStatusEntityRepository.findByStatusName(request.getStatus())).thenReturn(Optional.of(orderStatus));
        when(orderEntityRepository.findByOrderStatus(orderStatus)).thenReturn(List.of(orderEntity));
        when(orderConverter.convertEntityToNormal(orderEntity)).thenReturn(orderBase);

        GetOrdersResponse response = getOrdersByStatusImpl.getOrdersByStatus(request);

        assertNotNull(response);
        assertEquals(1, response.getAllOrders().size());
        assertEquals(orderBase, response.getAllOrders().get(0));
        verify(orderStatusEntityRepository).findByStatusName(request.getStatus());
        verify(orderEntityRepository).findByOrderStatus(orderStatus);
        verify(orderConverter).convertEntityToNormal(orderEntity);
    }

    @Test
    void givenInvalidStatus_whenStatusNotFound_thenThrowNotFoundException() {
        when(orderStatusEntityRepository.findByStatusName(request.getStatus())).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> {
            getOrdersByStatusImpl.getOrdersByStatus(request);
        });
        assertEquals("STATUS NOT FOUND", exception.getReason());
        verify(orderStatusEntityRepository).findByStatusName(request.getStatus());
    }

    @Test
    void givenValidStatus_whenNoOrdersFound_thenReturnEmptyList() {
        when(orderStatusEntityRepository.findByStatusName(request.getStatus())).thenReturn(Optional.of(orderStatus));
        when(orderEntityRepository.findByOrderStatus(orderStatus)).thenReturn(List.of());

        GetOrdersResponse response = getOrdersByStatusImpl.getOrdersByStatus(request);

        assertNotNull(response);
        assertTrue(response.getAllOrders().isEmpty());
        verify(orderStatusEntityRepository).findByStatusName(request.getStatus());
        verify(orderEntityRepository).findByOrderStatus(orderStatus);
    }


    @Test
    void givenValidStatusAndTakenOrder_whenLookingForOrderByStatusAndIsTaken_thenFoundOrders() {
        when(orderStatusEntityRepository.findByStatusName(requestIsTaken.getStatus())).thenReturn(Optional.of(orderStatus));
        when(orderEntityRepository.findByOrderStatusAndIsTaken(orderStatus, requestIsTaken.getIsTaken())).thenReturn(List.of(orderEntity));
        when(orderConverter.convertEntityToNormal(orderEntity)).thenReturn(orderBase);

        GetOrdersResponse response = getOrdersByStatusImpl.getOrdersByStatus(requestIsTaken);

        assertNotNull(response);
        assertEquals(1, response.getAllOrders().size());
        assertEquals(orderBase, response.getAllOrders().get(0));
        verify(orderStatusEntityRepository).findByStatusName(requestIsTaken.getStatus());
        verify(orderEntityRepository).findByOrderStatusAndIsTaken(orderStatus, requestIsTaken.getIsTaken());
        verify(orderConverter).convertEntityToNormal(orderEntity);
    }

}