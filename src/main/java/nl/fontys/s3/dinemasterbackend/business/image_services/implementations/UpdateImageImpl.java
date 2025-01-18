package nl.fontys.s3.dinemasterbackend.business.image_services.implementations;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.items.SaveImageRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.exceptions.OperationNotPossible;
import nl.fontys.s3.dinemasterbackend.business.image_services.UpdateImage;
import nl.fontys.s3.dinemasterbackend.persistence.entity.ItemEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.ItemEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateImageImpl implements UpdateImage {
    private final ImageService imageService;
    private final ItemEntityRepository itemEntityRepository;


    @Override
    @Transactional
    public void updateImage(SaveImageRequest request) {
        Optional<ItemEntity> foundItemEntityById = itemEntityRepository.findById(request.getItemId());
        if (foundItemEntityById.isPresent()) {
            if (imageService.deleteImage(foundItemEntityById.get().getItemImageUrl())) {
                Map<String, String> imageData = imageService.uploadImage(request.getImage());
                foundItemEntityById.get().setItemImageVersion("v" + imageData.get("version"));
                foundItemEntityById.get().setItemImageUrl(imageData.get("public_id"));
                itemEntityRepository.save(foundItemEntityById.get());
            } else {
                throw new OperationNotPossible("Updating image failed");
            }
        } else {
            throw new NotFound("ITEM_NOT_FOUND");
        }
    }
}
