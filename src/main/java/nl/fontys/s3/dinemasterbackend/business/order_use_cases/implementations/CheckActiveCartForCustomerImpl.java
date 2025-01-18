package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.CheckForCustomerActiveCartRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.CheckForCustomerActiveCartResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.CheckActiveCartForCustomer;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.persistence.entity.CartEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.CartEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CheckActiveCartForCustomerImpl implements CheckActiveCartForCustomer {
    private final CartEntityRepository cartEntityRepository;

    @Override
    public CheckForCustomerActiveCartResponse checkActiveCart(CheckForCustomerActiveCartRequest request, AccessToken accessToken) {
        Optional<CartEntity> activeCart = cartEntityRepository.findByCustomerIdAndIsActiveTrue(request.getCustomerId());

        if (activeCart.isPresent() && !activeCart.get().getCustomerId().equals(accessToken.getUserId())) {
            throw new AccessDenied("Customers have access only to their own carts.");
        }

        if (activeCart.isPresent()) {
            return CheckForCustomerActiveCartResponse.builder().thereIsActiveCart(true).activeCartId(activeCart.get().getCartId()).build();
        }
        return CheckForCustomerActiveCartResponse.builder().thereIsActiveCart(false).build();
    }
}

