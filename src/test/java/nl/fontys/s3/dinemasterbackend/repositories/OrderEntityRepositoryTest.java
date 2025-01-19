package nl.fontys.s3.dinemasterbackend.repositories;

import jakarta.persistence.EntityManager;
import nl.fontys.s3.dinemasterbackend.persistence.entity.CustomerEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderStatusEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.RoleEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class OrderEntityRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OrderEntityRepository orderEntityRepository;

    private OrderStatusEntity pendingStatus;
    private OrderStatusEntity deliveredStatus;
    private CustomerEntity customer;
    private OrderEntity order1;
    private OrderEntity order2;
    RoleEntity customerRole;

    @BeforeEach
    void setup() {
        // Set up order statuses
        pendingStatus = OrderStatusEntity.builder()
                .id(4L)
                .statusName("Pending")
                .build();
        deliveredStatus = OrderStatusEntity.builder()
                .id(4L)
                .statusName("Delivered")
                .build();
        pendingStatus = entityManager.merge(pendingStatus);
        deliveredStatus = entityManager.merge(deliveredStatus);

        customerRole = RoleEntity.builder()
                .id(1L)
                .name("Customer")
                .build();
        customerRole = entityManager.merge(customerRole);

        // Set up customer
        customer = CustomerEntity.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .phoneNumber("123456789")
                .role(customerRole)
                .build();
        customer = entityManager.merge(customer);

        // Set up orders
        order1 = OrderEntity.builder()
                .orderId(1L)
                .customerEntity(customer)
                .orderStatus(pendingStatus)
                .deliveryFee(5.0)
                .isTaken(false)
                .build();

        order2 = OrderEntity.builder()
                .orderId(2L)
                .customerEntity(customer)
                .orderStatus(deliveredStatus)
                .deliveryFee(5.0)
                .isTaken(true)
                .build();

        order1 = entityManager.merge(order1);
        order2 = entityManager.merge(order2);
    }

    @Test
    void testFindByOrderStatus() {
        List<OrderEntity> orders = orderEntityRepository.findByOrderStatus(pendingStatus);
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getOrderStatus()).isEqualTo(pendingStatus);
    }

    @Test
    void testFindByOrderStatusAndIsTaken() {
        List<OrderEntity> orders = orderEntityRepository.findByOrderStatusAndIsTaken(deliveredStatus, true);
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getIsTaken()).isTrue();
    }


    @Test
    void testFindByCustomerEntityUserId() {
        List<OrderEntity> orders = orderEntityRepository.findByCustomerEntityUserId(customer.getUserId(), PageRequest.of(0, 10));
        assertThat(orders).hasSize(2);
    }

    @Test
    void testCountByCustomerEntityUserId() {
        long count = orderEntityRepository.countByCustomerEntityUserId(customer.getUserId());
        assertThat(count).isEqualTo(2);
    }
}