package nl.fontys.s3.dinemasterbackend.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OperationNotPossible extends ResponseStatusException {
    public OperationNotPossible(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}


