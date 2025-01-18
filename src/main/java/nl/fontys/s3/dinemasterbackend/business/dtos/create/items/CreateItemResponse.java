package nl.fontys.s3.dinemasterbackend.business.dtos.create.items;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateItemResponse {
    private Long itemId;
}
