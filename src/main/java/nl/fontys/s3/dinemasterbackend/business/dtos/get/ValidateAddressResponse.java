package nl.fontys.s3.dinemasterbackend.business.dtos.get;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ValidateAddressResponse {
    private Boolean isValid;
    private String locationLongitude;
    private String locationLatitude;
}
