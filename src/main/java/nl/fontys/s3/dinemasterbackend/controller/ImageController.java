package nl.fontys.s3.dinemasterbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    /**
     * Endpoint for uploading an image for a specific item (Manager only).
     *
     * @param itemId The ID of the item for which the image is being uploaded.
     * @param image The image file to be uploaded for the item.
     * @return A 204 No Content response indicating successful image upload.
     */
    @Operation(
            summary = "Upload an image for an item (Manager only)",
            description = "This endpoint allows a Manager to upload an image for a specific item. " +
                    "Only users with the 'MANAGER' role can upload item images.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PostMapping("/{itemId}")
    public ResponseEntity<Void> saveImage(
            @Parameter(description = "The ID of the item to upload an image for")
            @PathVariable("itemId") Long itemId,

            @Parameter(description = "The image file to upload for the item")
            @RequestParam("image") MultipartFile image) {

        SaveImageRequest saveImageRequest = SaveImageRequest.builder()
                .itemId(itemId)
                .image(image)
                .build();

        saveImageImpl.saveImage(saveImageRequest);

        return ResponseEntity.noContent().build();
    }


    /**
     * Endpoint for updating an image for a specific item (Manager only).
     *
     * @param itemId The ID of the item for which the image is being updated.
     * @param image The new image file to update for the item.
     * @return A 204 No Content response indicating successful image update.
     */
    @Operation(
            summary = "Update an image for an item (Manager only)",
            description = "This endpoint allows a Manager to update an existing image for a specific item. " +
                    "Only users with the 'MANAGER' role can update item images.",
            tags = {"Item Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PutMapping("/{itemId}")
    public ResponseEntity<Void> updateImage(
            @Parameter(description = "The ID of the item to update the image for")
            @PathVariable("itemId") Long itemId,

            @Parameter(description = "The new image file to update for the item")
            @RequestParam("image") MultipartFile image) {

        SaveImageRequest saveImageRequest = SaveImageRequest.builder()
                .itemId(itemId)
                .image(image)
                .build();

        updateImageImpl.updateImage(saveImageRequest);

        return ResponseEntity.noContent().build();
    }

}
