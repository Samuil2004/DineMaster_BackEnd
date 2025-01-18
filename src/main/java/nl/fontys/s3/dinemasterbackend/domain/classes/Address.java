package nl.fontys.s3.dinemasterbackend.domain.classes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Address {
    private String street;

    private String city;

    private String postalCode;

    private String country;
}
