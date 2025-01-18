package nl.fontys.s3.dinemasterbackend.business.image_services.implementations;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.items.SaveImageRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.image_services.SaveImage;
import nl.fontys.s3.dinemasterbackend.persistence.entity.ItemEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.ItemEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SaveImageImpl implements SaveImage {
    private final ImageService imageService;
    private final ItemEntityRepository itemEntityRepository;

    @Override
    @Transactional
    public void saveImage(SaveImageRequest request) {
        Optional<ItemEntity> foundItemEntityById = itemEntityRepository.findById(request.getItemId());
        if (foundItemEntityById.isPresent()) {
            Map<String, String> imageData = imageService.uploadImage(request.getImage());
            foundItemEntityById.get().setItemImageVersion("v" + imageData.get("version"));
            foundItemEntityById.get().setItemImageUrl(imageData.get("public_id"));

            itemEntityRepository.save(foundItemEntityById.get());
        } else {
            throw new NotFound("ITEM_NOT_FOUND");
        }
    }
}
