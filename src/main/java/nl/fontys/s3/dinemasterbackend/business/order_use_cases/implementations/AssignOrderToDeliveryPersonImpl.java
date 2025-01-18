package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.UserConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.AssignOrderToDeliveryPersonRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.AssignOrderToDeliveryPerson;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.GetOrderById;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.GetUserById;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.domain.classes.User;
import nl.fontys.s3.dinemasterbackend.domain.enumerations.UserRole;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderDeliveryRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class AssignOrderToDeliveryPersonImpl implements AssignOrderToDeliveryPerson {
    private final GetUserById getUserById;
    private final OrderDeliveryRepository orderDeliveryRepository;
    private final UserConverter userConverter;
    private final GetOrderById getOrderById;
    private final OrderEntityRepository orderEntityRepository;

    @Override
    @Transactional
    public void assignOrderToDeliveryPerson(AssignOrderToDeliveryPersonRequest request, AccessToken accessToken) {
        if (accessToken.getRoles().stream().anyMatch(role -> role.equals("DELIVERY")) && !accessToken.getUserId().equals(request.getDeliveryPersonId())) {

            throw new AccessDenied("Delivery guys can only assign orders to themselves.");
        }

        User foundUser = getUserById.getUserByIdService(request.getDeliveryPersonId());
        if (foundUser.getUserRole() != UserRole.DELIVERY) {
            throw new NotFound("Delivery person with provided person was not found");
        }

        OrderEntity foundOrderById = getOrderById.getOrderByIdAndIsTaken(request.getOrderId(), false);
        if (!Objects.equals(foundOrderById.getOrderStatus().getStatusName(), "DELIVERING")) {
            throw new NotFound("Order ready to be delivered has not been found with the provided id");
        }
        UserEntity convertedUser = userConverter.convertNormalToEntity(foundUser);

        if (convertedUser instanceof StaffMemberEntity staffMemberEntity) {
            OrderDeliveryEntity newOrderDelivery = OrderDeliveryEntity.builder().deliveryPerson(staffMemberEntity).order(foundOrderById).build();
            orderDeliveryRepository.save(newOrderDelivery);
            foundOrderById.setIsTaken(true);
            orderEntityRepository.save(foundOrderById);
        }

    }
}
