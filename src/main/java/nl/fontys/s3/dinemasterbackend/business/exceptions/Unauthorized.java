package nl.fontys.s3.dinemasterbackend.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Unauthorized extends ResponseStatusException {
    public Unauthorized(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
