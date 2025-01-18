package nl.fontys.s3.dinemasterbackend.business.converters;

import nl.fontys.s3.dinemasterpro.domain.classes.Cart;
import nl.fontys.s3.dinemasterpro.domain.classes.Customer;
import nl.fontys.s3.dinemasterpro.domain.classes.Order;
import nl.fontys.s3.dinemasterpro.domain.classes.OrderStatus;
import nl.fontys.s3.dinemasterpro.persistence.entity.CartEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.CustomerEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.OrderEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.OrderStatusEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class OrderConverterTest {
    @InjectMocks
    private OrderConverter orderConverter;

    @Mock
    private CartConverter cartConverter;
    @Mock
    private UserConverter userConverter;
    @Mock
    private OrderStatusConverter orderStatusConverter;

    @Test
    void convertEntityToNormal_shouldConvertCorrectly() {
        CartEntity cartEntity = CartEntity.builder().cartId(1L).build();
        Cart cart = Cart.builder().cartId(1L).build();

        CustomerEntity customerEntity = CustomerEntity.builder().userId(1L).build();
        Customer customer = Customer.builder().userId(1L).build();

        OrderStatusEntity orderStatusEntity = OrderStatusEntity.builder().statusName("DELIVERED").build();
        OrderStatus orderStatus = OrderStatus.builder().statusName("DELIVERED").build();

        OrderEntity orderEntity = OrderEntity.builder()
                .orderId(100L)
                .cartEntity(cartEntity)
                .comments("Leave at the door")
                .customerEntity(customerEntity)
                .orderStatus(orderStatusEntity)
                .deliveryFee(5.99)
                .build();

        when(cartConverter.convertEntityToNormal(cartEntity)).thenReturn(cart);
        when(userConverter.convertEntityToCustomer(customerEntity)).thenReturn(customer);
        when(orderStatusConverter.convertEntityToNormal(orderStatusEntity)).thenReturn(orderStatus);

        Order order = orderConverter.convertEntityToNormal(orderEntity);

        assertNotNull(order);
        assertEquals(100L, order.getOrderId());
        assertEquals(cart, order.getCart());
        assertEquals("Leave at the door", order.getComments());
        assertEquals(customer, order.getCustomerWhoOrdered());
        assertEquals(orderStatus, order.getOrderStatus());
        assertEquals(5.99, order.getDeliveryFee());

        verify(cartConverter).convertEntityToNormal(cartEntity);
        verify(userConverter).convertEntityToCustomer(customerEntity);
        verify(orderStatusConverter).convertEntityToNormal(orderStatusEntity);
    }

    @Test
    void convertNormalToEntity_shouldConvertCorrectly() {
        Cart cart = Cart.builder().cartId(1L).build();
        CartEntity cartEntity = CartEntity.builder().cartId(1L).build();

        Customer customer = Customer.builder().userId(1L).build();
        CustomerEntity customerEntity = CustomerEntity.builder().userId(1L).build();

        OrderStatus orderStatus = OrderStatus.builder().statusName("DELIVERED").build();
        OrderStatusEntity orderStatusEntity = OrderStatusEntity.builder().statusName("DELIVERED").build();

        Order order = Order.builder()
                .orderId(100L)
                .cart(cart)
                .comments("Leave at the door")
                .customerWhoOrdered(customer)
                .orderStatus(orderStatus)
                .deliveryFee(5.99)
                .build();

        when(cartConverter.convertNormalToEntity(cart)).thenReturn(cartEntity);
        when(userConverter.convertCustomerToEntity(customer)).thenReturn(customerEntity);
        when(orderStatusConverter.convertNormalToEntity(orderStatus)).thenReturn(orderStatusEntity);

        OrderEntity orderEntity = orderConverter.convertNormalToEntity(order);

        assertNotNull(orderEntity);
        assertEquals(100L, orderEntity.getOrderId());
        assertEquals(cartEntity, orderEntity.getCartEntity());
        assertEquals("Leave at the door", orderEntity.getComments());
        assertEquals(customerEntity, orderEntity.getCustomerEntity());
        assertEquals(orderStatusEntity, orderEntity.getOrderStatus());
        assertEquals(5.99, orderEntity.getDeliveryFee());

        verify(cartConverter).convertNormalToEntity(cart);
        verify(userConverter).convertCustomerToEntity(customer);
        verify(orderStatusConverter).convertNormalToEntity(orderStatus);
    }
}