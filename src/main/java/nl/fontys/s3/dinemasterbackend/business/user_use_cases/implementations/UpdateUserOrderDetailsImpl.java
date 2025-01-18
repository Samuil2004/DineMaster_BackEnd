package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.AddressConverter;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.domain.classes.Address;
import nl.fontys.s3.dinemasterbackend.persistence.entity.AddressEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.CustomerEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.UserEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.UserEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UpdateUserOrderDetailsImpl implements nl.fontys.s3.dinemasterbackend.business.user_use_cases.UpdateUserOrderDetails {
    private final UserEntityRepository userEntityRepository;
    private final AddressConverter addressConverter;

    @Override
    @Transactional
    public void updateUserOrderDetails(Long userId, String phoneNumber, Address updatedAddress, AccessToken accessToken) {
        if (!accessToken.getUserId().equals(userId)) {
            throw new AccessDenied("Users can only update their own information.");
        }
        Optional<UserEntity> foundUserByEmail = userEntityRepository.findById(userId);
        if (foundUserByEmail.isPresent() && foundUserByEmail.get() instanceof CustomerEntity customerEntity) {
            AddressEntity addressEntity = addressConverter.convertNormalToEntity(updatedAddress);
            addressEntity.setCustomerDetails(customerEntity);
            customerEntity.setAddress(addressEntity);
            customerEntity.setPhoneNumber(phoneNumber);
            userEntityRepository.save(customerEntity);
        } else {
            throw new NotFound("USER_NOT_FOUND");
        }
    }
}
