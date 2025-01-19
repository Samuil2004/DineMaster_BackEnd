package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import nl.fontys.s3.dinemasterbackend.business.converters.OrderConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrderByIdRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrderByIdResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.domain.classes.Order;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOrderByIdImplTest {
    @Mock
    OrderEntityRepository orderEntityRepository;
    @Mock
    OrderConverter orderConverter;
    @InjectMocks
    GetOrderByIdImpl getOrderByIdImpl;

    GetOrderByIdRequest getOrderByIdRequest;
    OrderEntity orderEntity;
    GetOrderByIdResponse getOrderByIdResponse;
    Order orderBase;


    @BeforeEach
    void beforeEach() {
        getOrderByIdRequest = GetOrderByIdRequest.builder()
                .orderId(1L)
                .build();

        orderEntity = OrderEntity.builder()
                .orderId(1L)
                .cartEntity(null)
                .comments("No comments")
                .orderStatus(null)
                .customerEntity(null)
                .deliveryFee(5.00)
                .build();

        orderBase = Order.builder()
                .orderId(1L)
                .cart(null)
                .comments("No comments")
                .orderStatus(null)
                .customerWhoOrdered(null)
                .deliveryFee(5.00)
                .build();
        getOrderByIdResponse = GetOrderByIdResponse.builder()
                .order(orderBase)
                .build();
    }

    @Test
    void givenOrderId_whenGetOrderById_thenReturnFoundOrder() {
        when(orderEntityRepository.findById(getOrderByIdRequest.getOrderId())).thenReturn(Optional.ofNullable(orderEntity));
        when(orderConverter.convertEntityToNormal(orderEntity)).thenReturn(orderBase);

        GetOrderByIdResponse response = getOrderByIdImpl.getOrderById(getOrderByIdRequest);

        assertEquals(orderBase, response.getOrder());

        verify(orderConverter).convertEntityToNormal(orderEntity);
        verify(orderEntityRepository).findById(getOrderByIdRequest.getOrderId());
    }

    @Test
    void givenOrderId_whenOrderNotFound_thenReturnOrderNotFound() {
        when(orderEntityRepository.findById(getOrderByIdRequest.getOrderId())).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class,
                () -> getOrderByIdImpl.getOrderById(getOrderByIdRequest));


        assertEquals("ORDER_NOT_FOUND", exception.getReason());
        verify(orderEntityRepository).findById(getOrderByIdRequest.getOrderId());

    }

    @Test
    void givenOrderId_whenGetOrderByIdForInternalServer_thenReturnFoundOrder() {
        when(orderEntityRepository.findById(orderEntity.getOrderId())).thenReturn(Optional.ofNullable(orderEntity));

        OrderEntity response = getOrderByIdImpl.getOrderByIdInternalService(orderEntity.getOrderId());

        assertEquals(orderEntity, response);

        verify(orderEntityRepository).findById(getOrderByIdRequest.getOrderId());
    }

    @Test
    void givenOrderId_whenGetOrderByIdForInternalServerAndNoOrderIsFound_throwsAnError() {
        when(orderEntityRepository.findById(orderEntity.getOrderId())).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class,
                () -> getOrderByIdImpl.getOrderByIdInternalService(orderEntity.getOrderId()));


        assertEquals("ORDER_NOT_FOUND", exception.getReason());
        verify(orderEntityRepository).findById(getOrderByIdRequest.getOrderId());

    }

    @Test
    void givenOrderIdAndIsTakenStatus_whenGetOrderByIdAndIsTakenStatus_thenReturnFoundOrder() {
        when(orderEntityRepository.findByOrderIdAndIsTaken(orderEntity.getOrderId(), true)).thenReturn(Optional.ofNullable(orderEntity));

        OrderEntity response = getOrderByIdImpl.getOrderByIdAndIsTaken(orderEntity.getOrderId(), true);

        assertEquals(orderEntity, response);

        verify(orderEntityRepository).findByOrderIdAndIsTaken(orderEntity.getOrderId(), true);
    }

    @Test
    void givenOrderIdAndIsTakenStatus_whenGetOrderByIdAndIsTakenStatusAndNoOrderIsFound_throwsAnError() {
        when(orderEntityRepository.findByOrderIdAndIsTaken(orderEntity.getOrderId(), false)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class,
                () -> getOrderByIdImpl.getOrderByIdAndIsTaken(orderEntity.getOrderId(), false));

        assertEquals("ORDER_NOT_FOUND", exception.getReason());
        verify(orderEntityRepository).findByOrderIdAndIsTaken(orderEntity.getOrderId(), false);

    }
}