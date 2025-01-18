package nl.fontys.s3.dinemasterbackend.configuration.security.token;

public interface RefreshTokenEncoder {
    String encode(RefreshToken refreshToken);
}
