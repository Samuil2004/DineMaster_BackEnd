package nl.fontys.s3.dinemasterbackend.business.dtos.get.items;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class GetItemByIdRequest {

    @NotNull
    @Min(1)
    private Long itemId;
}
