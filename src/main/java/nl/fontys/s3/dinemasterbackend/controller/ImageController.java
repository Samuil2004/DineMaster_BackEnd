package nl.fontys.s3.dinemasterbackend.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.items.SaveImageRequest;
import nl.fontys.s3.dinemasterbackend.business.image_services.implementations.SaveImageImpl;
import nl.fontys.s3.dinemasterbackend.business.image_services.implementations.UpdateImageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
@AllArgsConstructor
public class ImageController {
    private final SaveImageImpl saveImageImpl;
    private final UpdateImageImpl updateImageImpl;

    //As a MANAGER only I should have access to upload item images
    @RolesAllowed({"MANAGER"})
    @PostMapping("/{itemId}")
    public ResponseEntity<Void> saveImage(
            @PathVariable("itemId") Long itemId,
            @RequestParam("image") MultipartFile image) {

        SaveImageRequest saveImageRequest = SaveImageRequest.builder()
                .itemId(itemId)
                .image(image)
                .build();

        saveImageImpl.saveImage(saveImageRequest);

        return ResponseEntity.noContent().build();
    }

    //As a MANAGER only I should have access to update item images
    @RolesAllowed({"MANAGER"})
    @PutMapping("/{itemId}")
    public ResponseEntity<Void> updateImage(
            @PathVariable("itemId") Long itemId,
            @RequestParam("image") MultipartFile image) {

        SaveImageRequest saveImageRequest = SaveImageRequest.builder()
                .itemId(itemId)
                .image(image)
                .build();

        updateImageImpl.updateImage(saveImageRequest);

        return ResponseEntity.noContent().build();
    }

}
