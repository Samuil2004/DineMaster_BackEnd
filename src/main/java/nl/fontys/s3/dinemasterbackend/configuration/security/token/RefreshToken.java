package nl.fontys.s3.dinemasterbackend.configuration.security.token;

public interface RefreshToken {
    String getSubject();

    Long getUserId();
}
