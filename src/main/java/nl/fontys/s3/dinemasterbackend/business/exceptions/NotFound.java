package nl.fontys.s3.dinemasterbackend.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFound extends ResponseStatusException {
    public NotFound(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
    }

