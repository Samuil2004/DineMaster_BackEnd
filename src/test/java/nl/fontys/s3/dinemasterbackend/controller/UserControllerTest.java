//package nl.fontys.s3.dinemasterbackend.controller;
//
//
//import nl.fontys.s3.dinemasterbackend.configuration.security.auth.RequestAuthenticatedUserProvider;
//import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
//import nl.fontys.s3.dinemasterbackend.configuration.security.token.implementations.AccessTokenImpl;
//import nl.fontys.s3.dinemasterbackend.persistence.repositories.UserEntityRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Set;
//
//import static org.hamcrest.Matchers.containsInAnyOrder;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//
//    @MockBean
//    private UserEntityRepository userEntityRepository;
//
//    @MockBean
//    private PasswordEncoder passwordEncoder;
//
//    @MockBean
//    private RequestAuthenticatedUserProvider requestAuthenticatedUserProvider;
//
//    private AccessToken accessTokenCustomer;
//
//
//    @BeforeEach
//    void setUp() {
//        Set<String> rolesCustomer = Set.of("CUSTOMER");
//        accessTokenCustomer = new AccessTokenImpl("customer@gmail.nl", 1L, rolesCustomer);
//    }
//
////    @Test
////    @WithMockUser(username = "customer@gmail.nl", roles = {"CUSTOMER"})
////    void givenCustomerAccountData_whenUpdatingCustomerAccount_thenUpdatesCustomerData() throws Exception {
////        Long customerId = 1L;
////
////        UpdateCustomerRequest request = UpdateCustomerRequest.builder()
////                .userId(1L)
////                .firstName("Michael")
////                .lastName("Jackson")
////                .password("Tester1234$")
////                .confirmPassword("Tester1234$")
////                .email("customer@gmail.nl")
////                .phoneNumber("+31987656734")
////                .country("Netherlands")
////                .city("Eindhoven")
////                .postalCode("5709DG")
////                .street("Street")
////                .build();
////
////        RoleEntity customerRole = RoleEntity.builder().id(4L).name("CUSTOMER").build();
////        AddressEntity addressEntity = AddressEntity.builder()
////                .country("Netherlands")
////                .city("Eindhoven")
////                .postalCode("5709DG")
////                .street("Street")
////                .build();
////        CustomerEntity customerEntity = CustomerEntity.builder()
////                .userId(1L)
////                .userId(1L)
////                .firstName("Michael")
////                .lastName("Jackson")
////                .password("Tester1234$")
////                .email("customer@gmail.nl")
////                .role(customerRole)
////                .phoneNumber("+31987656734")
////                .address(addressEntity)
////                .build();
////
////        when(requestAuthenticatedUserProvider.getAuthenticatedUserInRequest()).thenReturn(accessTokenCustomer);
////        when(userEntityRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));
////        when(userEntityRepository.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(customerEntity));
////        when(passwordEncoder.encode(request.getPassword())).thenReturn("Tester1234$");
////        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(customerEntity);
////
////        mockMvc.perform(put("/users/customers/1")
////                        .contentType(APPLICATION_JSON_VALUE)
////                        .content("""
////                                    {
////                                        "userId": 1,
////                                        "firstName": "Michael",
////                                        "lastName": "Jackson",
////                                        "password": "Tester1234$",
////                                        "confirmPassword": "Tester1234$",
////                                        "email": "customer@gmail.nl",
////                                        "phoneNumber": "+31987656734",
////                                        "country": "Netherlands",
////                                        "city": "Eindhoven",
////                                        "postalCode": "5709DG",
////                                        "street": "Street"
////                                    }
////                                """))
////                .andExpect(status().isNoContent());
////
////
////        verify(userEntityRepository).findByEmail(request.getEmail());
////        verify(passwordEncoder).encode(request.getPassword());
////        verify(userEntityRepository).save(any(UserEntity.class));
////    }
//
//    @Test
//    @WithMockUser(username = "customer@gmail.nl", roles = {"CUSTOMER"})
//    void givenCorruptedCustomerAccountData_whenUpdatingCustomerAccount_thenThrowsAnError400() throws Exception {
//
//        mockMvc.perform(put("/users/customers/1")
//                        .contentType(APPLICATION_JSON_VALUE)
//                        .content("""
//                                    {
//                                        "userId": 1,
//                                        "firstName": "Michael",
//                                        "lastName": "Jackson",
//                                        "password": "Tester1234$",
//                                        "confirmPassword": "Tester1234$",
//                                        "email": "customer-gmail.nl",
//                                        "phoneNumber": "+21345987656734",
//                                        "country": "",
//                                        "city": "Eindhoven",
//                                        "postalCode": "5709",
//                                        "street": "Street"
//                                    }
//                                """))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors[*].field").value(containsInAnyOrder(
//                        "email", "postalCode", "country", "phoneNumber"
//                )))
//                .andExpect(jsonPath("$.errors[*].error").value(containsInAnyOrder(
//                        "Invalid email format.",
//                        "Postal code must be a valid Dutch postal code.",
//                        "Country must not be blank.",
//                        "Invalid phone number. Must contain exactly 10 digits, e.g., +31XXXXXXXXX"
//                )));
//    }
//
//    @Test
//    @WithMockUser(username = "manager@gmail.nl", roles = {"MANAGER"})
//    void givenCorruptedParametersForLastName_whenGettingUsersByRoleAndLastName_thenThrowsAnError400() throws Exception {
//
//        mockMvc.perform(get("/users/getByRoleAndLastName/COOK")
//                        .param("lastName", " ")
//                        .param("pageNumber", "10"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @WithMockUser(username = "manager@gmail.nl", roles = {"MANAGER"})
//    void givenCorruptedParametersForPageNumber_whenGettingUsersByRoleAndLastName_thenThrowsAnError400() throws Exception {
//
//        mockMvc.perform(get("/users/getByRoleAndLastName/COOK")
//                        .param("lastName", "testName")
//                        .param("pageNumber", "-10"))
//                .andExpect(status().isBadRequest());
//    }
//
//}