package nl.fontys.s3.dinemasterbackend.business.converters;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.domain.classes.Customer;
import nl.fontys.s3.dinemasterbackend.domain.classes.StaffMember;
import nl.fontys.s3.dinemasterbackend.domain.classes.User;
import nl.fontys.s3.dinemasterbackend.domain.enumerations.UserRole;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.UserRoleEntityRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserConverter
{
    private final AddressConverter addressConverter;
    private final UserRoleEntityRepository userRoleEntityRepository;
    public User convertEntityToNormal(UserEntity entity)
    {
        if(entity instanceof CustomerEntity customerEntity)
        {
            return Customer.builder()
                    .userId(customerEntity.getUserId())
                    .firstName(customerEntity.getFirstName())
                    .lastName(customerEntity.getLastName())
                    .email(customerEntity.getEmail())
                    .userRole(UserRole.valueOf(customerEntity.getRole().getName()))
                    .phoneNumber(customerEntity.getPhoneNumber())
                    .address(addressConverter.convertEntityToNormal(customerEntity.getAddress()))
                    .password(customerEntity.getPassword())
                    .build();
        }
        if(entity instanceof StaffMemberEntity staffMemberEntity)
        {
            return StaffMember.builder()
                    .userId(staffMemberEntity.getUserId())
                    .firstName(staffMemberEntity.getFirstName())
                    .lastName(staffMemberEntity.getLastName())
                    .email(staffMemberEntity.getEmail())
                    .userRole(UserRole.valueOf(staffMemberEntity.getRole().getName()))
                    .password(staffMemberEntity.getPassword())
                    .staffId(staffMemberEntity.getStaffId())
                    .build();
        }
        return null;
    }

    public UserEntity convertNormalToEntity(User user)
    {
        Optional<RoleEntity> foundRole = userRoleEntityRepository.findByName(user.getUserRole().name());
        if(foundRole.isEmpty())
        {
            throw new NotFound("USER ROLE NOT FOUND");
        }
        if(user instanceof Customer customer)
        {
            CustomerEntity customerEntity = CustomerEntity.builder()
                    .userId(customer.getUserId())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .email(customer.getEmail())
                    .role(foundRole.get())
                    .phoneNumber(customer.getPhoneNumber())
                    .address(addressConverter.convertNormalToEntity(customer.getAddress()))
                    .password(customer.getPassword())
                    .build();
            customerEntity.getAddress().setCustomerDetails(customerEntity);
            return customerEntity;
        }
        if(user instanceof StaffMember staffMember)
        {
            return StaffMemberEntity.builder()
                    .userId(staffMember.getUserId())
                    .firstName(staffMember.getFirstName())
                    .lastName(staffMember.getLastName())
                    .email(staffMember.getEmail())
                    .role(foundRole.get())
                    .password(staffMember.getPassword())
                    .staffId(staffMember.getStaffId())
                    .build();
        }
        return null;
    }
    public Customer convertEntityToCustomer(CustomerEntity customerEntity) {
        return Customer.builder()
                .userId(customerEntity.getUserId())
                .firstName(customerEntity.getFirstName())
                .lastName(customerEntity.getLastName())
                .email(customerEntity.getEmail())
                .phoneNumber(customerEntity.getPhoneNumber())
                .userRole(UserRole.valueOf(customerEntity.getRole().getName()))
                .address(addressConverter.convertEntityToNormal(customerEntity.getAddress()))
                .password(customerEntity.getPassword())
                .build();
    }
    public CustomerEntity convertCustomerToEntity(Customer customer) {
        Optional<RoleEntity> foundRole = userRoleEntityRepository.findByName(customer.getUserRole().name());
        if(foundRole.isEmpty())
        {
            throw new NotFound("USER ROLE NOT FOUND");
        }
        return CustomerEntity.builder()
                .userId(customer.getUserId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .role(foundRole.get())
                .address(addressConverter.convertNormalToEntity(customer.getAddress()))
                .password(customer.getPassword())
                .build();
    }
}
