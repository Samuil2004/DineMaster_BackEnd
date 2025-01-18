package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.CheckForCustomerActiveOrderRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.CheckForCustomerActiveOrderResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.CheckActiveOrdersForCustomer;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class CheckActiveOrdersForCustomerImpl implements CheckActiveOrdersForCustomer {
    private final OrderEntityRepository orderEntityRepository;

    @Override
    public CheckForCustomerActiveOrderResponse checkForCustomerActiveOrder(CheckForCustomerActiveOrderRequest checkForCustomerActiveOrderRequest, AccessToken accessToken) {
        Optional<OrderEntity> activeOrders = orderEntityRepository.findOrderByCustomerIdAndStatusNotDelivered(checkForCustomerActiveOrderRequest.getCustomerId());

        if (activeOrders.isPresent() && !activeOrders.get().getCustomerEntity().getUserId().equals(accessToken.getUserId())) {
            throw new AccessDenied("Customers have access only to their own orders.");
        }

        if (activeOrders.isPresent()) {
            return CheckForCustomerActiveOrderResponse.builder().thereAreActiveOrders(true).activeOrderId(activeOrders.get().getOrderId()).build();
        }
        return CheckForCustomerActiveOrderResponse.builder().thereAreActiveOrders(false).build();
    }
}
