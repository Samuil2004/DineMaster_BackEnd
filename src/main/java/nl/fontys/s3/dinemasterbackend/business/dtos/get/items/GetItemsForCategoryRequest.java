package nl.fontys.s3.dinemasterbackend.business.dtos.get.items;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class GetItemsForCategoryRequest {
    @NotBlank(message = "Category must not be blank")
    private String category;

    private Boolean isVisibleInMenu;
}
