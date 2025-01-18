package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.CartConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetCartRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetCartResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.GetCart;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.domain.classes.Cart;
import nl.fontys.s3.dinemasterbackend.persistence.entity.CartEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.CartEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetCartImpl implements GetCart {
    private final CartEntityRepository cartEntityRepository;
    private final CartConverter cartConverter;

    @Override
    public GetCartResponse getCart(GetCartRequest request, AccessToken accessToken) {
        if (!request.getCustomerId().equals(accessToken.getUserId())) {
            throw new AccessDenied("Customers can only access their own carts.");
        }
        
        Optional<CartEntity> cartEntity = cartEntityRepository.findByCustomerIdAndIsActiveTrue(request.getCustomerId());

        if (cartEntity.isEmpty()) {
            throw new NotFound("CART_NOT_FOUND");
        }
        Cart cart = cartConverter.convertEntityToNormal(cartEntity.get());
        return GetCartResponse.builder().cart(cart).build();
    }
}
