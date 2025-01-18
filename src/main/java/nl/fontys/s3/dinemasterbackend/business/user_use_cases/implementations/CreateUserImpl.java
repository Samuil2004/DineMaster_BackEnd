package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.UserConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.users.CreateCustomerRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.users.CreateStaffMemberRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.users.CreateUserRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.users.CreateUserResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.exceptions.OperationNotPossible;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.CreateUser;
import nl.fontys.s3.dinemasterbackend.domain.classes.Address;
import nl.fontys.s3.dinemasterbackend.domain.classes.Customer;
import nl.fontys.s3.dinemasterbackend.domain.classes.StaffMember;
import nl.fontys.s3.dinemasterbackend.domain.classes.User;
import nl.fontys.s3.dinemasterbackend.domain.enumerations.UserRole;
import nl.fontys.s3.dinemasterbackend.persistence.entity.UserEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.UserEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.UserRoleEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateUserImpl implements CreateUser {
    private final UserConverter userConverter;
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleEntityRepository userRoleEntityRepository;


    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
        if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
            throw new OperationNotPossible("PASSWORDS DO NOT MATCH");
        }


        Optional<UserEntity> foundUserByEmail = userEntityRepository.findByEmail(createUserRequest.getEmail());
        if (foundUserByEmail.isPresent()) {
            throw new OperationNotPossible("USER WITH PROVIDED EMAIL ALREADY EXISTS");
        }

        User user = saveUser(createUserRequest);
        return CreateUserResponse.builder().userId(user.getUserId()).build();
    }

    private User saveUser(CreateUserRequest request) {
        if (request instanceof CreateCustomerRequest customerRequest) {
            Address customerAddress = Address.builder()
                    .country(customerRequest.getCountry())
                    .city(customerRequest.getCity())
                    .postalCode(customerRequest.getPostalCode())
                    .street(customerRequest.getStreet())
                    .build();
            Customer newCustomer = Customer.builder()
                    .firstName(customerRequest.getFirstName())
                    .lastName(customerRequest.getLastName())
                    .email(customerRequest.getEmail())
                    .userRole(UserRole.CUSTOMER)
                    .phoneNumber(customerRequest.getPhoneNumber())
                    .address(customerAddress)
                    .password(passwordEncoder.encode(customerRequest.getPassword()))
                    .build();

            UserEntity customerEntity = userConverter.convertNormalToEntity(newCustomer);
            UserEntity returnedEntity = userEntityRepository.save(customerEntity);
            return userConverter.convertEntityToNormal(returnedEntity);
        }
        if (request instanceof CreateStaffMemberRequest staffMemberRequest) {

            Optional<UserRole> foundRole = java.util.Arrays.stream(UserRole.values())
                    .filter(role -> role.name().equalsIgnoreCase(staffMemberRequest.getUserRole()))
                    .findFirst();

            if (foundRole.isEmpty()) {
                throw new NotFound("USER ROLE NOT FOUND");
            }

            StaffMember staffMember = StaffMember.builder()
                    .firstName(staffMemberRequest.getFirstName())
                    .lastName(staffMemberRequest.getLastName())
                    .email(staffMemberRequest.getEmail())
                    .userRole(foundRole.get())
                    .password(passwordEncoder.encode(staffMemberRequest.getPassword()))
                    .build();

            UserEntity staffMemberEntity = userConverter.convertNormalToEntity(staffMember);
            UserEntity returnedEntity = userEntityRepository.save(staffMemberEntity);

            return userConverter.convertEntityToNormal(returnedEntity);
        }
        throw new NotFound("USER_TYPE_NOT_FOUND");

    }
}
