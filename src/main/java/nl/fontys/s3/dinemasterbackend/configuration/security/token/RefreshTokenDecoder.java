package nl.fontys.s3.dinemasterbackend.configuration.security.token;

public interface RefreshTokenDecoder {
    RefreshToken decode(String refreshTokenEncoded);
}
