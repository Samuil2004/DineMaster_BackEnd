package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import nl.fontys.s3.dinemasterbackend.business.converters.OrderConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrdersForCustomerRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrdersResponse;
import nl.fontys.s3.dinemasterbackend.domain.classes.Order;
import nl.fontys.s3.dinemasterbackend.domain.classes.OrderStatus;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderStatusEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class GetOrdersForCustomerImplTest {

    @Mock
    OrderEntityRepository orderEntityRepository;
    @Mock
    OrderConverter orderConverter;
    @InjectMocks
    GetOrdersForCustomerImpl getOrdersForCustomerImpl;

    private OrderEntity orderEntity;
    private OrderStatusEntity orderStatus;
    private List<OrderEntity> orderEntities;
    private GetOrdersForCustomerRequest request;
    private OrderStatus orderStatusBase;
    private Order orderBase;
    private Pageable pageableFirstPage;


    @BeforeEach
    void setUp() {
        request = GetOrdersForCustomerRequest.builder().customerId(1L).pageNumber(1).build();
        orderStatus = OrderStatusEntity.builder().statusName("PREPARING").build();
        orderStatusBase = OrderStatus.builder().statusName("PREPARING").build();
        orderEntity = OrderEntity.builder().orderId(1L).orderStatus(orderStatus).build();
        orderBase = Order.builder().orderId(1L).orderStatus(orderStatusBase).build();
        orderEntities = List.of(orderEntity);
        pageableFirstPage = PageRequest.of(0, 10);

    }

    @Test
    void givenCustomerId_whenSearchingForOrdersForCustomer_thenReturnsAllOrdersForCustomer() {
        when(orderEntityRepository.findByCustomerEntityUserId(request.getCustomerId(), pageableFirstPage)).thenReturn(orderEntities);
        when(orderConverter.convertEntityToNormal(orderEntity)).thenReturn(orderBase);
        when(orderEntityRepository.countByCustomerEntityUserId(1L)).thenReturn(1L);

        GetOrdersResponse response = getOrdersForCustomerImpl.getOrdersForCustomer(request);
        assertEquals(orderBase, response.getAllOrders().get(0));
        verify(orderEntityRepository).findByCustomerEntityUserId(request.getCustomerId(), pageableFirstPage);
        verify(orderConverter).convertEntityToNormal(orderEntity);

    }

}