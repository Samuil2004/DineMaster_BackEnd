package nl.fontys.s3.dinemasterbackend.repositories;

import jakarta.persistence.EntityManager;
import nl.fontys.s3.dinemasterpro.persistence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class OrderDeliveryRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OrderDeliveryRepository orderDeliveryRepository;

    private StaffMemberEntity deliveryPerson;
    private OrderStatusEntity orderStatusPending;
    private OrderStatusEntity orderStatusDelivered;
    private OrderEntity order1;
    private OrderEntity order2;
    RoleEntity deliveryRole;

    @BeforeEach
    void setup() {
        deliveryRole = RoleEntity.builder()
                .id(1L)
                .name("Delivery Person")
                .build();

        deliveryRole = entityManager.merge(deliveryRole);

        deliveryPerson = StaffMemberEntity.builder()
                .staffId(1L)
                .firstName("Michael")
                .lastName("Jackson")
                .email("m.jackson@gmail.com")
                .password("Tester1234$")
                .role(deliveryRole)
                .build();
        deliveryPerson = entityManager.merge(deliveryPerson);

        orderStatusPending = OrderStatusEntity.builder()
                .id(1L)
                .statusName("Pending")
                .build();
        orderStatusDelivered = OrderStatusEntity.builder()
                .id(2L)
                .statusName("Delivered")
                .build();
        orderStatusPending = entityManager.merge(orderStatusPending);
        orderStatusDelivered = entityManager.merge(orderStatusDelivered);

        order1 = OrderEntity.builder()
                .orderId(1L)
                .comments("Order 1 comments")
                .orderStatus(orderStatusPending)
                .deliveryFee(5.0)
                .isTaken(false)
                .build();

        order2 = OrderEntity.builder()
                .orderId(2L)
                .comments("Order 2 comments")
                .orderStatus(orderStatusDelivered)
                .deliveryFee(5.0)
                .isTaken(false)
                .build();
        order1 = entityManager.merge(order1);
        order2 = entityManager.merge(order2);

        OrderDeliveryEntity delivery1 = OrderDeliveryEntity.builder()
                .id(1L)
                .order(order1)
                .deliveryPerson(deliveryPerson)
                .build();
        OrderDeliveryEntity delivery2 = OrderDeliveryEntity.builder()
                .id(2L)
                .order(order2)
                .deliveryPerson(deliveryPerson)
                .build();
        entityManager.merge(delivery1);
        entityManager.merge(delivery2);
    }

    @Test
    void findOrdersForDeliveryPersonWithStatusForDeliveryPaginated() {
        Pageable pageable = PageRequest.of(0, 10);
        List<OrderEntity> result = orderDeliveryRepository
                .findOrdersForDeliveryPersonWithStatusForDeliveryPaginated(
                        deliveryPerson.getUserId(),
                        orderStatusPending.getId(),
                        pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(order1.getOrderId(), result.get(0).getOrderId());
    }

    @Test
    void countTotalOrdersForDeliveryPerson() {
        long count = orderDeliveryRepository.countTotalOrdersForDeliveryPerson(
                deliveryPerson.getUserId(),
                orderStatusPending.getId());

        assertEquals(1, count);

        long countDelivered = orderDeliveryRepository.countTotalOrdersForDeliveryPerson(
                deliveryPerson.getUserId(),
                orderStatusDelivered.getId());

        assertEquals(1, countDelivered);
    }
}