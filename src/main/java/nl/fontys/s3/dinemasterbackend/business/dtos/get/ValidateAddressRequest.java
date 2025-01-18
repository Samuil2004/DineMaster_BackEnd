package nl.fontys.s3.dinemasterbackend.business.dtos.get;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ValidateAddressRequest {
    @NotBlank(message = "Country must not be blank.")
    private String country;

    @NotBlank(message = "City must not be blank.")
    private String city;

    @Pattern(regexp = "^[1-9]\\d{3}\\s?[A-Z]{2}$", message = "Postal code must be a valid Dutch postal code.")
    @NotBlank(message = "Postal code must not be blank.")
    private String postalCode;

    @NotBlank(message = "Street must not be blank.")
    private String street;
}
