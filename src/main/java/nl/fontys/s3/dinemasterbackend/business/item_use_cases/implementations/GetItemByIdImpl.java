package nl.fontys.s3.dinemasterbackend.business.item_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.ItemConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.items.GetItemByIdRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.items.GetItemByIdResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.item_use_cases.GetItemById;
import nl.fontys.s3.dinemasterbackend.domain.classes.Item;
import nl.fontys.s3.dinemasterbackend.persistence.entity.ItemEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.ItemEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetItemByIdImpl implements GetItemById {
    private final ItemEntityRepository itemEntityRepository;
    private final ItemConverter itemConverter;

    @Override
    public GetItemByIdResponse getItemById(GetItemByIdRequest request) {
        Optional<ItemEntity> itemEntity = itemEntityRepository.findById(request.getItemId());

        if (itemEntity.isEmpty())
        {
            throw new NotFound("ITEM_NOT_FOUND");
        }
        Item foundItem = itemConverter.convertEntityToNormal(itemEntity.get());
        return GetItemByIdResponse.builder().foundItem(foundItem).build();

    }
}
