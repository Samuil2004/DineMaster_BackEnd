package nl.fontys.s3.dinemasterbackend.business.converters;

import nl.fontys.s3.dinemasterpro.domain.classes.Address;
import nl.fontys.s3.dinemasterpro.persistence.entity.AddressEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AddressConverterTest {

    @InjectMocks
    AddressConverter addressConverter;

    @Test
    void convertEntityToNormal_shouldConvertCorrectly() {
        AddressEntity addressEntity = AddressEntity.builder()
                .street("Baker Street")
                .city("London")
                .postalCode("12345")
                .country("UK")
                .build();

        Address address = addressConverter.convertEntityToNormal(addressEntity);

        assertNotNull(address);
        assertEquals("Baker Street", address.getStreet());
        assertEquals("London", address.getCity());
        assertEquals("12345", address.getPostalCode());
        assertEquals("UK", address.getCountry());
    }

    @Test
    void convertNormalToEntity_shouldConvertCorrectly() {
        Address address = Address.builder()
                .street("Baker Street")
                .city("London")
                .postalCode("12345")
                .country("UK")
                .build();

        AddressEntity addressEntity = addressConverter.convertNormalToEntity(address);

        assertNotNull(addressEntity);
        assertEquals("Baker Street", addressEntity.getStreet());
        assertEquals("London", addressEntity.getCity());
        assertEquals("12345", addressEntity.getPostalCode());
        assertEquals("UK", addressEntity.getCountry());
    }
}