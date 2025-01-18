package nl.fontys.s3.dinemasterbackend.configuration.security.token.implementations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.RefreshToken;

@EqualsAndHashCode
@Getter
public class RefreshTokenImpl implements RefreshToken {
    private final String subject;
    private final Long userId;

    public RefreshTokenImpl(String subject, Long userId) {
        this.subject = subject;
        this.userId = userId;
    }
}
