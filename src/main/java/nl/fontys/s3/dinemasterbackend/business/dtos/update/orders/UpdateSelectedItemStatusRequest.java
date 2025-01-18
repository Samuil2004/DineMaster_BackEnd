package nl.fontys.s3.dinemasterbackend.business.dtos.update.orders;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class UpdateSelectedItemStatusRequest {

    @NotNull
    private Long selectedItemId;
}
