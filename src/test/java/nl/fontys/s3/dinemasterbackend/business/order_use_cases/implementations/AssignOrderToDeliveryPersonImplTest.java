package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import nl.fontys.s3.dinemasterbackend.business.converters.UserConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.AssignOrderToDeliveryPersonRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.GetOrderById;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.GetUserById;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.implementations.AccessTokenImpl;
import nl.fontys.s3.dinemasterbackend.domain.classes.User;
import nl.fontys.s3.dinemasterbackend.domain.enumerations.UserRole;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderDeliveryRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AssignOrderToDeliveryPersonImplTest {
    @InjectMocks
    private AssignOrderToDeliveryPersonImpl assignOrderToDeliveryPerson;

    @Mock
    private GetUserById getUserById;

    @Mock
    private OrderDeliveryRepository orderDeliveryRepository;

    @Mock
    private UserConverter userConverter;

    @Mock
    private GetOrderById getOrderById;

    @Mock
    private OrderEntityRepository orderEntityRepository;

    private AssignOrderToDeliveryPersonRequest request;
    private AccessToken accessToken;
    private User foundUser;
    private StaffMemberEntity staffMemberEntity;
    private OrderEntity foundOrderById;
    private OrderStatusEntity orderStatusEntity;

    @BeforeEach
    void setUp() {
        request = AssignOrderToDeliveryPersonRequest.builder()
                .deliveryPersonId(1L)
                .orderId(100L)
                .build();

        Set<String> roleNames = new HashSet<>();
        roleNames.add(
                "CUSTOMER"
        );
        accessToken = new AccessTokenImpl("test@test.com", 1L, roleNames);

        foundUser = User.builder()
                .userId(1L)
                .userRole(UserRole.DELIVERY)
                .build();

        staffMemberEntity = StaffMemberEntity.builder().userId(1L).build();

        orderStatusEntity = OrderStatusEntity.builder()
                .statusName("DELIVERING")
                .build();

        foundOrderById = OrderEntity.builder()
                .orderId(100L)
                .isTaken(false)
                .orderStatus(orderStatusEntity)
                .build();
    }

    @Test
    void givenDeliveryRoleButDifferentUserId_whenAssignOrder_thenThrowAccessDenied() {
        Set<String> roleNames = new HashSet<>();
        roleNames.add(
                "DELIVERY"
        );
        accessToken = new AccessTokenImpl("test@test.com", 3L, roleNames);


        AccessDenied exception = assertThrows(AccessDenied.class, () -> {
            assignOrderToDeliveryPerson.assignOrderToDeliveryPerson(request, accessToken);
        });

        assertEquals("Delivery guys can only assign orders to themselves.", exception.getReason());
    }


    @Test
    void givenUserNotDeliveryRole_whenAssignOrder_thenThrowNotFound() {
        foundUser.setUserRole(UserRole.MANAGER);
        when(getUserById.getUserByIdService(request.getDeliveryPersonId())).thenReturn(foundUser);

        NotFound exception = assertThrows(NotFound.class, () -> {
            assignOrderToDeliveryPerson.assignOrderToDeliveryPerson(request, accessToken);
        });

        assertEquals("Delivery person with provided person was not found", exception.getReason());
        verify(getUserById).getUserByIdService(request.getDeliveryPersonId());
    }

    @Test
    void givenOrderNotDeliveringStatus_whenAssignOrder_thenThrowNotFound() {
        orderStatusEntity.setStatusName("PREPARING");
        when(getUserById.getUserByIdService(request.getDeliveryPersonId())).thenReturn(foundUser);
        when(getOrderById.getOrderByIdAndIsTaken(request.getOrderId(), false)).thenReturn(foundOrderById);

        NotFound exception = assertThrows(NotFound.class, () -> {
            assignOrderToDeliveryPerson.assignOrderToDeliveryPerson(request, accessToken);
        });

        assertEquals("Order ready to be delivered has not been found with the provided id", exception.getReason());
        verify(getUserById).getUserByIdService(request.getDeliveryPersonId());
        verify(getOrderById).getOrderByIdAndIsTaken(request.getOrderId(), false);
    }

    @Test
    void givenValidRequest_whenAssignOrder_thenSuccess() {
        when(getUserById.getUserByIdService(request.getDeliveryPersonId())).thenReturn(foundUser);
        when(getOrderById.getOrderByIdAndIsTaken(request.getOrderId(), false)).thenReturn(foundOrderById);
        when(userConverter.convertNormalToEntity(foundUser)).thenReturn(staffMemberEntity);

        assignOrderToDeliveryPerson.assignOrderToDeliveryPerson(request, accessToken);

        verify(orderEntityRepository).save(foundOrderById);
        assertTrue(foundOrderById.getIsTaken());
    }
}