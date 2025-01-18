package nl.fontys.s3.dinemasterbackend.business.dtos.get.items;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public class GetItemFromMenuRequest {

    @NotNull
    @Min(1)
    public Long itemId;
}
