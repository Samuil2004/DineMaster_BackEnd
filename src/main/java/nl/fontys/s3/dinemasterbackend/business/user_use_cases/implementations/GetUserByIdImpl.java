package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.UserConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.GetUserByIdRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.GetUserByIdResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.GetUserById;
import nl.fontys.s3.dinemasterbackend.domain.classes.Customer;
import nl.fontys.s3.dinemasterbackend.domain.classes.StaffMember;
import nl.fontys.s3.dinemasterbackend.domain.classes.User;
import nl.fontys.s3.dinemasterbackend.domain.enumerations.UserRole;
import nl.fontys.s3.dinemasterbackend.persistence.entity.UserEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.UserEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetUserByIdImpl implements GetUserById {
    private final UserEntityRepository userEntityRepository;
    private final UserConverter userConverter;

    @Override
    public GetUserByIdResponse getUserById(GetUserByIdRequest request) {

        //As a delivery I can see only customer's personal information and my personal information
        if (request.getAccessToken().getRoles().stream().anyMatch(role -> role.equals("DELIVERY"))) {
            Optional<UserEntity> userEntity = userEntityRepository.findById(request.getUserId());

            if (userEntity.isEmpty() ||
                    (!userEntity.get().getRole().getName().equals(UserRole.CUSTOMER.name()) &&
                            !userEntity.get().getUserId().equals(request.getAccessToken().getUserId()))) {
                throw new AccessDenied("Delivery users can only access their own information or customer information.");
            }
        }

        //As a customer I can see only my personal information
        if (request.getAccessToken().getRoles().stream().anyMatch(role -> role.equals("CUSTOMER")) && !request.getAccessToken().getUserId().equals(request.getUserId())) {

            throw new AccessDenied("Customers can only access their own information.");
        }

        Optional<UserEntity> userEntity = userEntityRepository.findById(request.getUserId());
        if (userEntity.isEmpty()) {
            throw new NotFound("USER_NOT_FOUND");
        }

        User foundUser = userConverter.convertEntityToNormal(userEntity.get());
        GetUserByIdResponse response = GetUserByIdResponse.builder()
                .userId(foundUser.getUserId())
                .username(foundUser.getEmail())
                .firstName(foundUser.getFirstName())
                .lastName(foundUser.getLastName())
                .userRole(foundUser.getUserRole())
                .build();

        if (foundUser instanceof Customer customer) {
            response.setPhoneNumber(customer.getPhoneNumber());
            response.setAddress(customer.getAddress());
        } else if (foundUser instanceof StaffMember staffMember) {
            response.setStaffId(staffMember.getStaffId());
        }

        return response;
    }

    @Override
    public User getUserByIdService(Long userId) {
        Optional<UserEntity> userEntity = userEntityRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new NotFound("USER_NOT_FOUND");
        }
        return userConverter.convertEntityToNormal(userEntity.get());
    }

}
