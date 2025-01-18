package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.OrderConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.GetOrdersForDeliveryPersonResponse;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrdersForDeliveryPersonRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.GetOrdersForDeliveryPerson;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.GetUserById;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.domain.classes.Order;
import nl.fontys.s3.dinemasterbackend.domain.classes.User;
import nl.fontys.s3.dinemasterbackend.domain.enumerations.UserRole;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderDeliveryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class GetOrdersForDeliveryPersonImpl implements GetOrdersForDeliveryPerson {
    private final OrderDeliveryRepository orderDeliveryRepository;
    private final OrderConverter orderConverter;
    private final GetUserById getUserById;

    @Override
    public GetOrdersForDeliveryPersonResponse getActiveOrdersForDeliveryPerson(GetOrdersForDeliveryPersonRequest request, AccessToken accessToken) {
        int pageNumber;
        if (request.getPageNumber() == null) {
            pageNumber = 1;
        } else {
            pageNumber = request.getPageNumber();
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, 10);

        Set<String> roles = accessToken.getRoles();
        Long userIdFromAccessToken = accessToken.getUserId();

        if (!roles.contains("MANAGER") && roles.contains("DELIVERY") && !userIdFromAccessToken.equals(request.getDeliveryPersonId())) {
            throw new AccessDenied("Delivery personnel can only view their own orders.");
        }

        User foundUser = getUserById.getUserByIdService(request.getDeliveryPersonId());
        if (foundUser.getUserRole() != UserRole.DELIVERY) {
            throw new NotFound("Delivery person with provided person was not found");
        }


        List<OrderEntity> allOrdersForDeliveryForDeliveryPerson = orderDeliveryRepository.findOrdersForDeliveryPersonWithStatusForDeliveryPaginated(request.getDeliveryPersonId(), request.getStatusId(), pageable);
        if (allOrdersForDeliveryForDeliveryPerson.isEmpty()) {
            throw new NotFound("No orders for delivery were found");
        }

        long totalNumberOfOrdersForDeliveryPerson = orderDeliveryRepository.countTotalOrdersForDeliveryPerson(request.getDeliveryPersonId(), request.getStatusId());
        List<Order> ordersBase = allOrdersForDeliveryForDeliveryPerson.stream().map(orderConverter::convertEntityToNormal).toList();
        return GetOrdersForDeliveryPersonResponse.builder().allOrdersAssignedToToADeliveryPerson(ordersBase).totalCount(totalNumberOfOrdersForDeliveryPerson).build();
    }
}
