package nl.fontys.s3.dinemasterbackend.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AccessDenied extends ResponseStatusException {
    public AccessDenied(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
