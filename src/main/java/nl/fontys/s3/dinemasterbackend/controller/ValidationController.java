package nl.fontys.s3.dinemasterbackend.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.CalculateDeliveryFeeResponse;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.ValidateAddressRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.ValidateAddressResponse;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.CalculateDeliveryFee;
import nl.fontys.s3.dinemasterbackend.business.validation_services.ValidateAddress;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validation")
@AllArgsConstructor
public class ValidationController {

    private final ValidateAddress validateAddress;
    private final CalculateDeliveryFee calculateDeliveryFee;

    //As a CUSTOMER I should have access to the algorithm that validates the address
    @RolesAllowed({"CUSTOMER"})
    @PostMapping("/address")
    public ResponseEntity<ValidateAddressResponse> validateAddress(@RequestBody @Valid ValidateAddressRequest validateAddressRequest) {
        ValidateAddressResponse response = validateAddress.isAddressValid(validateAddressRequest);
        return ResponseEntity.ok(response);
    }

    //As a CUSTOMER I should have access to the algorithm that calculates the delivery fee
    @RolesAllowed({"CUSTOMER"})
    @PostMapping("/delivery-fee")
    public ResponseEntity<CalculateDeliveryFeeResponse> calculateDeliveryFeeBasedOnProvidedEndAddress(@RequestBody @Valid ValidateAddressRequest validateAddressRequest) {
        CalculateDeliveryFeeResponse response = calculateDeliveryFee.calculateDeliveryFee(validateAddressRequest);
        return ResponseEntity.ok(response);
    }

}
