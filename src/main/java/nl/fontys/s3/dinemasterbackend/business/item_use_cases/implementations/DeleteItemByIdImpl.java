package nl.fontys.s3.dinemasterbackend.business.item_use_cases.implementations;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.delete.DeleteItemRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.exceptions.OperationNotPossible;
import nl.fontys.s3.dinemasterbackend.business.image_services.implementations.ImageService;
import nl.fontys.s3.dinemasterbackend.business.item_use_cases.DeleteItemById;
import nl.fontys.s3.dinemasterbackend.persistence.entity.ItemEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.SelectedItemEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.ItemEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.SelectedItemEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DeleteItemByIdImpl implements DeleteItemById {
    private final ItemEntityRepository itemEntityRepository;
    private final ImageService imageService;
    private final SelectedItemEntityRepository selectedItemEntityRepository;

    @Override
    @Transactional
    public void deleteItemById(DeleteItemRequest request) {

        Optional<ItemEntity> itemEntity = itemEntityRepository.findById(request.getItemId());

        if (itemEntity.isEmpty()) {
            throw new NotFound("Item with ID " + request.getItemId() + " from " + request.getCategory() + " not found");
        }

        List<SelectedItemEntity> foundItemsInOrders = selectedItemEntityRepository.findSelectedItemsInActiveCarts(itemEntity.get());
        if (foundItemsInOrders.isEmpty()) {
            itemEntityRepository.deleteById(itemEntity.get().getItemId());
            imageService.deleteImage(itemEntity.get().getItemImageUrl());
        } else {
            throw new OperationNotPossible("Operation is not possible at the moment. There are active orders containing the selected item");
        }

    }

    @Override
    @Transactional
    public void deleteItemFromOrders(DeleteItemRequest deleteItemRequest) {
        Optional<ItemEntity> itemEntity = itemEntityRepository.findById(deleteItemRequest.getItemId());

        if (itemEntity.isEmpty()) {
            throw new NotFound("Item with ID " + deleteItemRequest.getItemId() + " from " + deleteItemRequest.getCategory() + " not found");
        }

        selectedItemEntityRepository.deleteByItemOfReferenceId(deleteItemRequest.getItemId());
    }
}
