package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import nl.fontys.s3.dinemasterpro.business.dtos.get.users.HandleForgottenPasswordRequest;
import nl.fontys.s3.dinemasterpro.business.email_services.SendEmail;
import nl.fontys.s3.dinemasterpro.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterpro.persistence.entity.CustomerEntity;
import nl.fontys.s3.dinemasterpro.persistence.repositories.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ForgottenPasswordImplTest {
    @Mock
    private SendEmail sendEmail;
    @Mock
    private UserEntityRepository userEntityRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PasswordGeneratorImpl passwordGenerator;

    @InjectMocks
    private ForgottenPasswordImpl forgottenPassword;

    private HandleForgottenPasswordRequest request;
    private CustomerEntity userEntity;

    @BeforeEach
    void setUp() {
        request = HandleForgottenPasswordRequest.builder()
                .email("test@example.com")
                .build();

        userEntity = CustomerEntity.builder()
                .email("test@example.com")
                .password("oldPassword")
                .build();
    }

    @Test
    void givenExistingUser_whenHandleForgottenPassword_thenSendEmailAndUpdatePassword() {
        String generatedPassword = "newGeneratedPassword";
        String encodedPassword = "encodedNewPassword";

        when(userEntityRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(userEntity));
        when(passwordGenerator.generateValidPassword()).thenReturn(generatedPassword);
        when(passwordEncoder.encode(generatedPassword)).thenReturn(encodedPassword);

        forgottenPassword.handleForgottenPassword(request);

        verify(userEntityRepository).findByEmail(request.getEmail());
        verify(passwordGenerator).generateValidPassword();
        verify(passwordEncoder).encode(generatedPassword);
        verify(sendEmail).sendEmail(eq(request.getEmail()), eq("Password reset"), anyString());
        verify(userEntityRepository).save(userEntity);

        assertEquals(encodedPassword, userEntity.getPassword());
    }

    @Test
    void givenNonExistentUser_whenHandleForgottenPassword_thenThrowNotFound() {
        when(userEntityRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () ->
                forgottenPassword.handleForgottenPassword(request)
        );

        assertEquals("User with provided email not found", exception.getReason());
        verify(userEntityRepository).findByEmail(request.getEmail());
        verifyNoInteractions(passwordGenerator, passwordEncoder, sendEmail);
    }

    @Test
    void givenValidUser_whenSendEmailContent_thenEmailBodyIsCorrect() {
        String generatedPassword = "newGeneratedPassword";

        when(userEntityRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(userEntity));
        when(passwordGenerator.generateValidPassword()).thenReturn(generatedPassword);
        when(passwordEncoder.encode(generatedPassword)).thenReturn("encodedNewPassword");

        forgottenPassword.handleForgottenPassword(request);

        verify(sendEmail).sendEmail(request.getEmail(), "Password reset",
                "Hello test@example.com, here is the new password newGeneratedPassword that you can use to access your account. Please make sure to update it once you log in!");
    }
}