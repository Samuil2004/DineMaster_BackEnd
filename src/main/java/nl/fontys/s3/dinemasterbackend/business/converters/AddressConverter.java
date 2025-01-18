package nl.fontys.s3.dinemasterbackend.business.converters;

import nl.fontys.s3.dinemasterbackend.domain.classes.Address;
import nl.fontys.s3.dinemasterbackend.persistence.entity.AddressEntity;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter {
    public Address convertEntityToNormal(AddressEntity entity)
    {
        return Address.builder()
                .street(entity.getStreet())
                .city(entity.getCity())
                .postalCode(entity.getPostalCode())
                .country(entity.getCountry())
                .build();
    }
    public AddressEntity convertNormalToEntity (Address address)
    {
        return AddressEntity.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .build();
    }
}
