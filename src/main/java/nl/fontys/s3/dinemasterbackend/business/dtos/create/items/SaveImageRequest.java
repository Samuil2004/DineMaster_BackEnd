package nl.fontys.s3.dinemasterbackend.business.dtos.create.items;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaveImageRequest {
    @NotNull(message = "Item id, must not be null")
    private Long itemId;
    private MultipartFile image;
}
