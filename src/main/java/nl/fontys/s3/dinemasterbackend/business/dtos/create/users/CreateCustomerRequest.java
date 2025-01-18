package nl.fontys.s3.dinemasterbackend.business.dtos.create.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode(callSuper = true)
public class CreateCustomerRequest extends CreateUserRequest {

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+31\\d{9}$",
            message = "Invalid phone number. Must contain exactly 10 digits, e.g., +31XXXXXXXXX"
    )
    private String phoneNumber;

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
