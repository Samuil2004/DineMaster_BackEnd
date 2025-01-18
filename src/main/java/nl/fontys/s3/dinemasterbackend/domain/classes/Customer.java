package nl.fontys.s3.dinemasterbackend.domain.classes;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
public class Customer extends User {
    private String phoneNumber;
    private Address address;
}
