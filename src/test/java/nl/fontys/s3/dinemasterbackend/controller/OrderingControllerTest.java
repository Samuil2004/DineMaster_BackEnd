package nl.fontys.s3.dinemasterbackend.controller;

import nl.fontys.s3.dinemasterbackend.configuration.security.auth.RequestAuthenticatedUserProvider;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.implementations.AccessTokenImpl;
import nl.fontys.s3.dinemasterbackend.domain.classes.*;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.CartEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartEntityRepository cartEntityRepository;


    @MockBean
    private OrderEntityRepository orderEntityRepository;

    @MockBean
    private RequestAuthenticatedUserProvider requestAuthenticatedUserProvider;

    private AccessToken accessTokenCustomer;
    private AccessToken accessTokenCustomerId2;

    OrderEntity orderEntity;
    CartEntity cartEntity;
    Cart cartBase;
    CustomerEntity customer;
    RoleEntity customerRole;
    AddressEntity addressEntity;

    @BeforeEach
    void setUp() {
        Set<String> rolesCustomer = Set.of("CUSTOMER");
        accessTokenCustomer = new AccessTokenImpl("customer@test.com", 1L, rolesCustomer);

        accessTokenCustomerId2 = new AccessTokenImpl("customer2@test.com", 2L, rolesCustomer);


        cartEntity = CartEntity.builder()
                .cartId(1L)
                .customerId(1L)
                .price(50.0)
                .selectedItemEntities(new ArrayList<>())
                .isActive(true)
                .build();

        cartBase = Cart.builder()
                .cartId(1L)
                .customerId(1L)
                .price(50.0)
                .selectedItems(new ArrayList<>())
                .isActive(true)
                .build();

        customerRole = RoleEntity.builder().id(4L).name("CUSTOMER").build();
        addressEntity = AddressEntity.builder()
                .country("Netherlands")
                .city("Eindhoven")
                .postalCode("5709DG")
                .street("Street")
                .build();

        customer = CustomerEntity.builder()
                .userId(1L)
                .firstName("Michael")
                .lastName("Jackson")
                .password("Tester1234$")
                .email("customer@gmail.nl")
                .role(customerRole)
                .phoneNumber("+31987656734")
                .address(addressEntity)
                .build();

        orderEntity = OrderEntity.builder().orderId(1L).customerEntity(customer).build();


    }

    @Test
    @WithMockUser(username = "customer@fontys.nl", roles = {"CUSTOMER"})
    void givenCustomerCart_whenGettingCartForCustomer_thenReturnCart() throws Exception {
        Long customerId = 1L;

        when(requestAuthenticatedUserProvider.getAuthenticatedUserInRequest()).thenReturn(accessTokenCustomer);
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(1L)).thenReturn(Optional.ofNullable(cartEntity));

        mockMvc.perform(get("/carts-active/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {
                            "cart": {
                                "cartId": 1,
                                "customerId": 1,
                                "price": 50.0,
                                "isActive": true
                            }
                        }
                        """));

        verify(requestAuthenticatedUserProvider).getAuthenticatedUserInRequest();
        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(1L);
    }

    @Test
    @WithMockUser(username = "customer@fontys.nl", roles = {"CUSTOMER"})
    void givenCustomerCart_whenGettingCartForCustomerWithInvalidAccessToken_thenThrowsUnauthorized() throws Exception {
        Long customerId = 1L;

        when(requestAuthenticatedUserProvider.getAuthenticatedUserInRequest()).thenReturn(null);

        mockMvc.perform(get("/carts-active/{customerId}", customerId))
                .andExpect(status().isUnauthorized());

        verify(requestAuthenticatedUserProvider).getAuthenticatedUserInRequest();
    }

    @Test
    @WithMockUser(username = "customer@fontys.nl", roles = {"CUSTOMER"})
    void givenCustomerCartWithIdDifferentFromTheAccessTokenCustomerID_whenGettingCartForCustomer_thenThrowsAccessDenied() throws Exception {
        Long customerId = 1L;

        when(requestAuthenticatedUserProvider.getAuthenticatedUserInRequest()).thenReturn(accessTokenCustomerId2);
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(1L)).thenReturn(Optional.ofNullable(cartEntity));

        mockMvc.perform(get("/carts-active/{customerId}", customerId))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors[0].error").value("Customers can only access their own carts."));
    }

    @Test
    @WithMockUser(username = "cook@fontys.nl", roles = {"COOK"})
    void whenGetCartForCustomerWithCookRole_thenReturnAccessDenied() throws Exception {
        Long customerId = 1L;

        mockMvc.perform(get("/carts-active/{customerId}", customerId))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenGettingCartForCustomerWithInvalidAccessToken_thenReturnsUnauthorized() throws Exception {
        Long customerId = 1L;

        when(requestAuthenticatedUserProvider.getAuthenticatedUserInRequest()).thenReturn(null);

        mockMvc.perform(get("/carts-active/{customerId}", customerId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "customer@fontys.nl", roles = {"CUSTOMER"})
    void givenCustomerActiveOrder_whenCheckingForCustomerActiveOrder_thenReturnsActiveOrderStatus() throws Exception {
        Long customerId = 1L;
        when(requestAuthenticatedUserProvider.getAuthenticatedUserInRequest()).thenReturn(accessTokenCustomer);
        when(orderEntityRepository.findOrderByCustomerIdAndStatusNotDelivered(1L)).thenReturn(Optional.ofNullable(orderEntity));

        mockMvc.perform(get("/orders/{customerId}/active", customerId))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "thereAreActiveOrders": true,
                            "activeOrderId": 1
                        }
                        """));
        verify(requestAuthenticatedUserProvider).getAuthenticatedUserInRequest();
        verify(orderEntityRepository).findOrderByCustomerIdAndStatusNotDelivered(1L);
    }


    @Test
    @WithMockUser(username = "customer@fontys.nl", roles = {"CUSTOMER"})
    void createOrder_WhenAccessTokenIsInvalid_thenThrowsUnauthorized() throws Exception {
        Long customerId = 1L;

        when(requestAuthenticatedUserProvider.getAuthenticatedUserInRequest()).thenReturn(null);

        mockMvc.perform(post("/cart/{customerId}/checkout", customerId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                    "customerId": 1,
                                    "cartId": 1,
                                    "comments": "Please deliver between 6-7 PM",
                                    "phoneNumber": "+31123456789",
                                    "country": "Netherlands",
                                    "city": "Eindhoven",
                                    "postalCode": "1234 AB",
                                    "street": "Main Street"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }
}