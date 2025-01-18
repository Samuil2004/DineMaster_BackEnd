package nl.fontys.s3.dinemasterbackend.controller;

import nl.fontys.s3.dinemasterpro.business.dtos.create.users.CreateCustomerRequest;
import nl.fontys.s3.dinemasterpro.business.dtos.create.users.CreateUserResponse;
import nl.fontys.s3.dinemasterpro.business.dtos.get.AccessTokenResponse;
import nl.fontys.s3.dinemasterpro.business.dtos.get.users.AuthenticateUserRequest;
import nl.fontys.s3.dinemasterpro.business.user_use_cases.AuthenticateUser;
import nl.fontys.s3.dinemasterpro.business.user_use_cases.CreateUser;
import nl.fontys.s3.dinemasterpro.configuration.security.auth.RequestAuthenticatedUserProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateUser createUser;


    @MockBean
    private AuthenticateUser authenticateUser;

    @MockBean
    private RequestAuthenticatedUserProvider requestAuthenticatedUserProvider;


    @Test
    void givenLogInCredentials_whenLogIn_thenReturnsAccessToken() throws Exception {
        AuthenticateUserRequest request = AuthenticateUserRequest.builder()
                .username("customer@gmail.nl")
                .password("Password123$")
                .build();


        AccessTokenResponse expectedResponse = AccessTokenResponse.builder()
                .accessToken("mocked-access-token")
                .build();

        when(authenticateUser.authenticateUser(request)).thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/logIn")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                    "username": "customer@gmail.nl",
                                    "password": "Password123$"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mocked-access-token"));

        verify(authenticateUser).authenticateUser(request);
    }

    @Test
    void givenLogInWithCorruptedPassword_whenLogIn_thenReturnsAccessToken() throws Exception {

        mockMvc.perform(post("/auth/logIn")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                    "username": "customer@gmail.nl",
                                    "password": "password123$"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("password"))
                .andExpect(jsonPath("$.errors[0].error").value("Password must include at least one lowercase letter, one uppercase letter, one number, one special character, and no spaces."));

    }

    @Test
    void givenLogInWithCorruptedUsername_whenLogIn_thenReturnsBadRequest() throws Exception {

        mockMvc.perform(post("/auth/logIn")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                    "username": "not real",
                                    "password": "Password123$"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("username"))
                .andExpect(jsonPath("$.errors[0].error").value("Invalid email format."));

    }

    @Test
    void givenCustomerData_whenCreatingAccountForCustomer_thenReturnsCreatedUserId() throws Exception {
        CreateCustomerRequest request = CreateCustomerRequest.builder()
                .firstName("Michael")
                .lastName("Jackson")
                .password("Password123$")
                .confirmPassword("Password123$")
                .email("michael@example.com")
                .phoneNumber("+31123456789")
                .country("Netherlands")
                .city("Amsterdam")
                .postalCode("1234 AB")
                .street("Main Street 1")
                .build();

        CreateUserResponse expectedResponse = CreateUserResponse.builder()
                .userId(1L)
                .build();

        when(createUser.createUser(request)).thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/customers")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                    "firstName": "Michael",
                                    "lastName": "Jackson",
                                    "password": "Password123$",
                                    "confirmPassword": "Password123$",
                                    "email": "michael@example.com",
                                    "phoneNumber": "+31123456789",
                                    "country": "Netherlands",
                                    "city": "Amsterdam",
                                    "postalCode": "1234 AB",
                                    "street": "Main Street 1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    void givenCustomerDataWithCorruptedFirstName_whenCreatingAccountForCustomer_thenThrowsBadRequest() throws Exception {
        CreateCustomerRequest request = CreateCustomerRequest.builder()
                .firstName("Michael4")
                .lastName("Jackson")
                .password("Password123$")
                .confirmPassword("Password123$")
                .email("michael@example.com")
                .phoneNumber("+31123456789")
                .country("Netherlands")
                .city("Amsterdam")
                .postalCode("1234 AB")
                .street("Main Street 1")
                .build();

        CreateUserResponse expectedResponse = CreateUserResponse.builder()
                .userId(1L)
                .build();

        when(createUser.createUser(request)).thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/customers")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                    "firstName": "Michael4",
                                    "lastName": "Jackson",
                                    "password": "Password123$",
                                    "confirmPassword": "Password123$",
                                    "email": "michael@example.com",
                                    "phoneNumber": "+31123456789",
                                    "country": "Netherlands",
                                    "city": "Amsterdam",
                                    "postalCode": "1234 AB",
                                    "street": "Main Street 1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("firstName"))
                .andExpect(jsonPath("$.errors[0].error").value("First name must contain only alphabetical characters"));

    }


}