package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.CartConverter;
import nl.fontys.s3.dinemasterbackend.business.converters.OrderConverter;
import nl.fontys.s3.dinemasterbackend.business.converters.OrderStatusConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.CreateOrderRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.CreateOrderResponse;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.CalculateDeliveryFeeResponse;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.ValidateAddressRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.exceptions.OperationNotPossible;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.CalculateDeliveryFee;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.CreateOrder;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.GetUserById;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.UpdateUserOrderDetails;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.domain.classes.Address;
import nl.fontys.s3.dinemasterbackend.domain.classes.Customer;
import nl.fontys.s3.dinemasterbackend.domain.classes.Order;
import nl.fontys.s3.dinemasterbackend.domain.classes.User;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.CartEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderStatusEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.SelectedItemStatusOfPreparationEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateOrderImpl implements CreateOrder {
    private final OrderEntityRepository orderEntityRepository;
    private final GetUserById getUserById;
    private final CartEntityRepository cartEntityRepository;
    private final OrderStatusEntityRepository orderStatusEntityRepository;
    private final SelectedItemStatusOfPreparationEntityRepository selectedItemStatusOfPreparationEntityRepository;
    private final CalculateDeliveryFee calculateDeliveryFee;
    private final OrderConverter orderConverter;
    private final CartConverter cartConverter;
    private final OrderStatusConverter orderStatusConverter;
    private final UpdateUserOrderDetails updateUserOrderDetails;

    private void updateUsersData(Customer customer, CreateOrderRequest createOrderRequest, AccessToken accessToken) {
        Address newAddress = Address.builder()
                .city(createOrderRequest.getCity())
                .street(createOrderRequest.getStreet())
                .country(createOrderRequest.getCountry())
                .postalCode(createOrderRequest.getPostalCode())
                .build();
        updateUserOrderDetails.updateUserOrderDetails(customer.getUserId(), createOrderRequest.getPhoneNumber(), newAddress, accessToken);

    }

    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest, AccessToken accessToken) {

        if (!createOrderRequest.getCustomerId().equals(accessToken.getUserId())) {
            throw new AccessDenied("Customers can only create orders for themselves.");
        }

        ValidateAddressRequest validateAddressRequest = ValidateAddressRequest.builder()
                .city(createOrderRequest.getCity())
                .country(createOrderRequest.getCountry())
                .street(createOrderRequest.getStreet())
                .postalCode(createOrderRequest.getPostalCode())
                .build();

        CalculateDeliveryFeeResponse calculateDeliveryFeeResponse = calculateDeliveryFee.calculateDeliveryFee(validateAddressRequest);

        User userById = getUserById.getUserByIdService(createOrderRequest.getCustomerId());
        if (userById instanceof Customer customer) {

            updateUsersData(customer, createOrderRequest, accessToken);

            Order createOrder = saveOrder(createOrderRequest, calculateDeliveryFeeResponse.getDeliveryFee(), customer);
            return CreateOrderResponse.builder().order(createOrder).build();
        }
        throw new NotFound("USER NOT FOUND");

    }

    private Order saveOrder(CreateOrderRequest createOrderRequest, Double deliveryFee, Customer customer) {
        CartEntity cartEntity = findAndValidateCart(customer, createOrderRequest.getCartId());

        cartEntity.setIsActive(false);
        updateSelectedItemsWithinCartStatus(cartEntity);

        CartEntity returnedAfterUpdatedStatus = cartEntityRepository.save(cartEntity);

        return createAndSaveOrder(createOrderRequest, returnedAfterUpdatedStatus, customer, deliveryFee);
    }


    private CartEntity findAndValidateCart(Customer customer, Long cartId) {
        Optional<CartEntity> cartEntity = cartEntityRepository.findByCustomerIdAndIsActiveTrue(customer.getUserId());
        if (cartEntity.isEmpty()) {
            throw new NotFound("CART NOT FOUND");
        }
        CartEntity cartEntityByRequestId = cartEntityRepository.findById(cartId).orElse(null);
        if (cartEntityByRequestId != null && cartEntity.get() == cartEntityByRequestId) {
            return cartEntity.get();
        }
        throw new NotFound("CART NOT FOUND");
    }

    private void updateSelectedItemsWithinCartStatus(CartEntity cartEntity) {
        Optional<SelectedItemStatusOfPreparationEntity> foundStatus = selectedItemStatusOfPreparationEntityRepository.findByStatusName("PREPARING");
        if (foundStatus.isEmpty()) {
            throw new OperationNotPossible("SELECTED STATUS NOT FOUND");
        }
        for (SelectedItemEntity selectedItemEntity : cartEntity.getSelectedItemEntities()) {
            selectedItemEntity.setStatusOfPreparation(foundStatus.get());
        }
    }

    private Order createAndSaveOrder(CreateOrderRequest createOrderRequest, CartEntity cartEntity, Customer customer, Double deliveryFee) {
        Optional<OrderStatusEntity> foundStatusInRepo = orderStatusEntityRepository.findByStatusName("PREPARING");
        if (foundStatusInRepo.isEmpty()) {
            throw new OperationNotPossible("SELECTED STATUS NOT FOUND");
        }

        Order newOrder = Order.builder()
                .cart(cartConverter.convertEntityToNormal(cartEntity))
                .comments(createOrderRequest.getComments())
                .customerWhoOrdered(customer)
                .orderStatus(orderStatusConverter.convertEntityToNormal(foundStatusInRepo.get()))
                .deliveryFee(deliveryFee)
                .isTaken(false)
                .build();

        OrderEntity orderEntityFromRequest = orderConverter.convertNormalToEntity(newOrder);
        OrderEntity returnedOrder = orderEntityRepository.save(orderEntityFromRequest);
        return orderConverter.convertEntityToNormal(returnedOrder);
    }

}
