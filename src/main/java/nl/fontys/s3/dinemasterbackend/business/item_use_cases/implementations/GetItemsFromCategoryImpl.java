package nl.fontys.s3.dinemasterbackend.business.item_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.ItemCategoryConverter;
import nl.fontys.s3.dinemasterbackend.business.converters.ItemConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.items.GetItemsForCategoryRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.items.GetItemsForCategoryResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.item_use_cases.GetItemsFromCategory;
import nl.fontys.s3.dinemasterbackend.domain.classes.ItemCategory;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.ItemCategoryRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.ItemEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetItemsFromCategoryImpl implements GetItemsFromCategory {
    private final ItemEntityRepository itemEntityRepository;
    private final ItemConverter itemConverter;
    private final ItemCategoryConverter itemCategoryConverter;
    private final ItemCategoryRepository itemCategoryRepository;

    private ItemCategory findCategory(String categoryName) {
        Optional<ItemCategoryEntity> foundCategory = itemCategoryRepository.findByCategoryName(categoryName);
        if (foundCategory.isPresent()) {
            return itemCategoryConverter.convertEntityToNormal(foundCategory.get());
        }
        throw new NotFound("ITEM_CATEGORY_NOT_FOUND");
    }

    @Override
    public GetItemsForCategoryResponse readItemsByCategory(GetItemsForCategoryRequest request) {

        ItemCategory selectedCategory = findCategory(request.getCategory().toUpperCase());
        List<ItemEntity> foundInRepo;
        if (request.getIsVisibleInMenu()) {
            foundInRepo = itemEntityRepository.findByItemCategory_CategoryIdAndVisibleInMenu(selectedCategory.getCategoryId(), true);
        } else {
            foundInRepo = itemEntityRepository.findByItemCategory_CategoryId(selectedCategory.getCategoryId());
        }
        return GetItemsForCategoryResponse
                .builder()
                .itemsInCategory(foundInRepo.stream().map(itemConverter::convertEntityToNormal)
                        .toList())
                .build();

    }
}
