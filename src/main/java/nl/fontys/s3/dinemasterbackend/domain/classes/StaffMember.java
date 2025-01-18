package nl.fontys.s3.dinemasterbackend.domain.classes;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
public class StaffMember extends User {
    private Long staffId;
}
