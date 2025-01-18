package nl.fontys.s3.dinemasterbackend.business.dtos.create.orders;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CreateOrderRequest {

    @NotNull(message = "Customer ID must not be null.")
    @Min(value = 1, message = "Customer ID must be at least 1.")
    private Long customerId;

    @NotNull(message = "Cart ID must not be null.")
    @Min(value = 1, message = "Cart ID must be at least 1.")
    private Long cartId;

    @NotBlank(message = "Comments must not be blank.")
    private String comments;

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
