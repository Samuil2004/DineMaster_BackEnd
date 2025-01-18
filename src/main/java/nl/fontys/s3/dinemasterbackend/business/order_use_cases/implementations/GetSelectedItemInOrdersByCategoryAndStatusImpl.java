package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.SelectedItemConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetSelectedItemsInOrdersByCategoryAndStatusRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetSelectedItemsInOrdersByCategoryAndStatusResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.GetSelectedItemsInOrdersByCategoryAndStatus;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.ItemCategoryRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.SelectedItemEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.SelectedItemStatusOfPreparationEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetSelectedItemInOrdersByCategoryAndStatusImpl implements GetSelectedItemsInOrdersByCategoryAndStatus {
    private final SelectedItemEntityRepository selectedItemEntityRepository;
    private final SelectedItemStatusOfPreparationEntityRepository selectedItemStatusOfPreparationEntityRepository;
    private final ItemCategoryRepository itemCategoryRepository;

    private final SelectedItemConverter selectedItemConverter;

    @Override
    public GetSelectedItemsInOrdersByCategoryAndStatusResponse getSelectedItemsInOrdersByCategoryAndStatus(GetSelectedItemsInOrdersByCategoryAndStatusRequest request) {
        Optional<SelectedItemStatusOfPreparationEntity> foundStatus = selectedItemStatusOfPreparationEntityRepository.findByStatusName(request.getStatus().toUpperCase());
        Optional<ItemCategoryEntity> selectedCategory = itemCategoryRepository.findByCategoryName(request.getCategory().toUpperCase());
        if(selectedCategory.isPresent() && foundStatus.isPresent()) {
            List<SelectedItemEntity> foundSelectedItems = selectedItemEntityRepository.findByStatusOfPreparation_IdAndItemCategory_CategoryId(foundStatus.get().getId(),selectedCategory.get().getCategoryId());
            return GetSelectedItemsInOrdersByCategoryAndStatusResponse.builder()
                    .selectedItems(foundSelectedItems.stream().map(selectedItemConverter::convertEntityToNormal).toList())
                    .build();
        }
        throw  new NotFound("SELECTED ITEMS FOR GIVEN STATUS AND CATEGORY WERE NOT FOUND");
    }
}
