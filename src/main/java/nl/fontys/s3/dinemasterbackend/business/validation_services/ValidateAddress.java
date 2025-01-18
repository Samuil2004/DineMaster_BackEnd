package nl.fontys.s3.dinemasterbackend.business.validation_services;

import nl.fontys.s3.dinemasterbackend.business.dtos.get.ValidateAddressRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.ValidateAddressResponse;

public interface ValidateAddress {
    ValidateAddressResponse isAddressValid(ValidateAddressRequest request);
}
