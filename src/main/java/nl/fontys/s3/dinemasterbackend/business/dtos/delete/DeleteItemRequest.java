package nl.fontys.s3.dinemasterbackend.business.dtos.delete;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DeleteItemRequest {

    @NotBlank(message = "Category must not be blank.")
    private String category;

    @NotNull(message = "Item ID must not be null.")
    @Min(value = 1, message = "Item ID must be at least 1.")
    private Long itemId;
}
