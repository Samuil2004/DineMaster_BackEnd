package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.HandleForgottenPasswordRequest;
import nl.fontys.s3.dinemasterbackend.business.email_services.SendEmail;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.ForgottenPassword;
import nl.fontys.s3.dinemasterbackend.persistence.entity.UserEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.UserEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ForgottenPasswordImpl implements ForgottenPassword {
    private final SendEmail sendEmail;
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGeneratorImpl passwordGenerator;

    @Override
    @Transactional
    public void handleForgottenPassword(HandleForgottenPasswordRequest request) {
        Optional<UserEntity> userWithProvidedEmail = userEntityRepository.findByEmail(request.getEmail());
        if (userWithProvidedEmail.isEmpty()) {
            throw new NotFound("User with provided email not found");
        }
        String newPassword = passwordGenerator.generateValidPassword();
        String encodedPassword = passwordEncoder.encode(newPassword);
        String emailBodyMessage = String.format(
                "Hello %s, here is the new password %s that you can use to access your account. Please make sure to update it once you log in!",
                request.getEmail(),
                newPassword
        );
        sendEmail.sendEmail(request.getEmail(), "Password reset", emailBodyMessage);

        userWithProvidedEmail.get().setPassword(encodedPassword);
        userEntityRepository.save(userWithProvidedEmail.get());
    }
}
