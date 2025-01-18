package nl.fontys.s3.dinemasterbackend.configuration.security.token;

import java.util.Set;

public interface AccessToken {
    String getSubject();
    Set<String> getRoles();
    Long getUserId();
}
