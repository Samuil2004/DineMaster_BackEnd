package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.UserConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.update.users.UpdateCustomerRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.update.users.UpdateStaffMemberRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.update.users.UpdateUserRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.exceptions.OperationNotPossible;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.UpdateUser;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.domain.classes.Address;
import nl.fontys.s3.dinemasterbackend.domain.classes.Customer;
import nl.fontys.s3.dinemasterbackend.domain.classes.StaffMember;
import nl.fontys.s3.dinemasterbackend.domain.classes.User;
import nl.fontys.s3.dinemasterbackend.persistence.entity.UserEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.UserEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateUserImpl implements UpdateUser {
    private final UserConverter userConverter;
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void updateUser(UpdateUserRequest request, AccessToken accessToken) {

        //As a user I can update only my personal information
        if (!accessToken.getUserId().equals(request.getUserId())) {
            throw new AccessDenied("Users can only update their own information.");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new OperationNotPossible("PASSWORDS DO NOT MATCH");
        }
        Optional<UserEntity> userEntity = userEntityRepository.findById(request.getUserId());
        if (userEntity.isEmpty()) {
            throw new NotFound("USER_NOT_FOUND");
        }
        Optional<UserEntity> foundUserByEmail = userEntityRepository.findByEmail(request.getEmail());
        if (foundUserByEmail.isPresent() && !Objects.equals(foundUserByEmail.get().getUserId(), request.getUserId())) {
            throw new OperationNotPossible("USER WITH PROVIDED EMAIL ALREADY EXISTS");
        }

        User newUser = userConverter.convertEntityToNormal(userEntity.get());
        UpdateFields(request, newUser);
    }

    private void UpdateFields(UpdateUserRequest request, User user) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (user instanceof Customer customer && request instanceof UpdateCustomerRequest customerRequest) {
            Address newAddress = Address.builder()
                    .country(customerRequest.getCountry())
                    .city(customerRequest.getCity())
                    .postalCode(customerRequest.getPostalCode())
                    .street(customerRequest.getStreet())
                    .build();

            customer.setPhoneNumber(customerRequest.getPhoneNumber());
            customer.setAddress(newAddress);
            customer.setUserRole(user.getUserRole());
            UserEntity userEntity = userConverter.convertNormalToEntity(customer);
            userEntityRepository.save(userEntity);
        } else if (user instanceof StaffMember staffMember && request instanceof UpdateStaffMemberRequest staffMemberRequest) {
            staffMember.setStaffId(staffMemberRequest.getStaffId());
            staffMember.setUserRole(user.getUserRole());
            UserEntity staffEntity = userConverter.convertNormalToEntity(staffMember);
            userEntityRepository.save(staffEntity);

        } else {
            throw new NotFound("USER_NOT_FOUND");
        }
    }

}
