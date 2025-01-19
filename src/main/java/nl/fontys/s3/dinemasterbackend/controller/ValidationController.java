package nl.fontys.s3.dinemasterbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    /**
     * Endpoint for validating a customer's address.
     *
     * @param validateAddressRequest The address details to validate.
     * @return A response indicating whether the provided address is valid.
     */
    @Operation(
            summary = "Validate address (Customer only)",
            description = "This endpoint allows customers to validate a provided address. " +
                    "The address is checked for validity to ensure it is in a deliverable location.",
            tags = {"Address Validation"}
    )
    @RolesAllowed({"CUSTOMER"})
    @PostMapping("/address")
    public ResponseEntity<ValidateAddressResponse> validateAddress(
            @Parameter(description = "The address details to validate")
            @RequestBody @Valid ValidateAddressRequest validateAddressRequest) {

        ValidateAddressResponse response = validateAddress.isAddressValid(validateAddressRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for calculating the delivery fee based on the provided address.
     *
     * @param validateAddressRequest The address details to calculate the delivery fee.
     * @return A response containing the calculated delivery fee.
     */
    @Operation(
            summary = "Calculate delivery fee (Customer only)",
            description = "This endpoint allows customers to calculate the delivery fee based on the provided address. " +
                    "The fee is determined by the distance between the restaurant and the customer's address.",
            tags = {"Delivery Fee Calculation"}
    )
    @RolesAllowed({"CUSTOMER"})
    @PostMapping("/delivery-fee")
    public ResponseEntity<CalculateDeliveryFeeResponse> calculateDeliveryFeeBasedOnProvidedEndAddress(
            @Parameter(description = "The address details to calculate the delivery fee")
            @RequestBody @Valid ValidateAddressRequest validateAddressRequest) {

        CalculateDeliveryFeeResponse response = calculateDeliveryFee.calculateDeliveryFee(validateAddressRequest);
        return ResponseEntity.ok(response);
    }

}
