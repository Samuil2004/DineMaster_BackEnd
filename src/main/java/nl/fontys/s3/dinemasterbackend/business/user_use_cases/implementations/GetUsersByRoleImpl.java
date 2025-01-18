package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.UserConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.GetUserByRoleRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.GetUserByRoleResponse;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.GetUserByRole;
import nl.fontys.s3.dinemasterbackend.persistence.entity.UserEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.UserEntityRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
public class GetUsersByRoleImpl implements GetUserByRole {
    private final UserEntityRepository userEntityRepository;
    private final UserConverter userConverter;

    @Override
    public GetUserByRoleResponse getUserByRoleAndLastName(GetUserByRoleRequest request) {
        Pageable pageable = PageRequest.of(request.getPageNumber() - 1, 10);
        List<UserEntity> foundUsersWithProvidedRole;
        long totalResults;

        if (request.getUsername() == null) {
            foundUsersWithProvidedRole = userEntityRepository.findByRoleName(request.getRole(), pageable);
            totalResults = userEntityRepository.countByRoleName(request.getRole());
        } else {
            foundUsersWithProvidedRole = userEntityRepository.findByRole_NameAndLastNameContainingIgnoreCase(request.getRole(), request.getUsername(), pageable);
            totalResults = userEntityRepository.countByRole_NameAndLastNameContainingIgnoreCase(request.getRole(), request.getUsername());
        }
        return GetUserByRoleResponse.builder().users(foundUsersWithProvidedRole.stream().map(userConverter::convertEntityToNormal).toList()).totalResults(totalResults).build();
    }

    @Override
    public GetUserByRoleResponse getUserByRoleAndUsername(GetUserByRoleRequest request) {
        Pageable pageable = PageRequest.of(request.getPageNumber() - 1, 10);
        List<UserEntity> foundUsersWithProvidedRole;
        long totalResults;

        if (request.getUsername() == null) {
            foundUsersWithProvidedRole = userEntityRepository.findByRoleName(request.getRole(), pageable);
            totalResults = userEntityRepository.countByRoleName(request.getRole());
        } else {
            foundUsersWithProvidedRole = userEntityRepository.findByRole_nameAndEmailContainingIgnoreCase(request.getRole(), request.getUsername(), pageable);
            totalResults = userEntityRepository.countByRole_nameAndEmailContainingIgnoreCase(request.getRole(), request.getUsername());
        }
        return GetUserByRoleResponse.builder().users(foundUsersWithProvidedRole.stream().map(userConverter::convertEntityToNormal).toList()).totalResults(totalResults).build();
    }
}
