package nl.fontys.s3.dinemasterbackend.configuration.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);

}
