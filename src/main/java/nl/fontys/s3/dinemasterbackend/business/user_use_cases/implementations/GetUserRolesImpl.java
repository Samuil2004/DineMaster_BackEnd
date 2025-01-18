package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.GetUserRolesResponse;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.GetUserRoles;
import nl.fontys.s3.dinemasterbackend.persistence.entity.RoleEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.UserRoleEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class GetUserRolesImpl implements GetUserRoles {
    private final UserRoleEntityRepository userRoleEntityRepository;

    @Override
    public GetUserRolesResponse getUserRoles() {
        List<RoleEntity> allUserRoles = userRoleEntityRepository.findAll();
        List<String> roleNames = allUserRoles.stream()
                .map(RoleEntity::getName)
                .toList();
        return GetUserRolesResponse.builder()
                .allUserRolesNames(roleNames)
                .build();
    }
}
