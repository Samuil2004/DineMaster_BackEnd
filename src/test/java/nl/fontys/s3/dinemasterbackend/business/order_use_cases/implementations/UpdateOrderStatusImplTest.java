package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import nl.fontys.s3.dinemasterbackend.business.dtos.update.orders.UpdateOrderStatusRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateOrderStatusImplTest {

    @InjectMocks
    private UpdateOrderStatusImpl updateOrderStatusImpl;

    @Mock
    private OrderEntityRepository orderEntityRepository;

    @Mock
    private OrderStatusEntityRepository orderStatusEntityRepository;

    private UpdateOrderStatusRequest request;
    private OrderEntity orderEntity;
    private OrderStatusEntity orderStatus;

    @BeforeEach
    void setUp() {
        request = UpdateOrderStatusRequest.builder().orderId(1L).status("PREPARING").build();

        orderEntity = OrderEntity.builder().orderId(1L).build();

        orderStatus = OrderStatusEntity.builder().statusName("PREPARING").build();
    }

    @Test
    void givenValidOrder_whenStatusExists_thenUpdateOrderStatus() {
        when(orderEntityRepository.findById(1L)).thenReturn(Optional.of(orderEntity));
        when(orderStatusEntityRepository.findByStatusName(request.getStatus())).thenReturn(Optional.of(orderStatus));

        updateOrderStatusImpl.updateOrderStatus(request);

        verify(orderEntityRepository).save(orderEntity);
        assertEquals(orderStatus, orderEntity.getOrderStatus());
    }

    @Test
    void givenValidOrderWithInvalidStatus_whenStatusDoesNotExist_thenThrowNotFoundException() {
        when(orderEntityRepository.findById(1L)).thenReturn(Optional.of(orderEntity));
        when(orderStatusEntityRepository.findByStatusName(request.getStatus())).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> {
            updateOrderStatusImpl.updateOrderStatus(request);
        });
        assertEquals("SELECTED STATUS NOT FOUND", exception.getReason());
    }

    @Test
    void givenInvalidOrder_whenOrderNotFound_thenThrowNotFoundException() {
        when(orderEntityRepository.findById(1L)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> {
            updateOrderStatusImpl.updateOrderStatus(request);
        });
        assertEquals("ORDER WAS NOT FOUND", exception.getReason());
    }

}