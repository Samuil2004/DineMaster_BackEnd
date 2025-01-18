package nl.fontys.s3.dinemasterbackend.business.converters;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.domain.classes.SelectedItemStatusOfPreparation;
import nl.fontys.s3.dinemasterbackend.persistence.entity.SelectedItemStatusOfPreparationEntity;
import org.springframework.stereotype.Component;
@Component
@AllArgsConstructor
public class SelectedItemStatusOfPreparationConverter {
    public SelectedItemStatusOfPreparation convertEntityToNormal(SelectedItemStatusOfPreparationEntity entity)
    {
        return SelectedItemStatusOfPreparation.builder()
                .id(entity.getId())
                .statusName(entity.getStatusName())
                .build();
    }
    public SelectedItemStatusOfPreparationEntity convertNormalToEntity (SelectedItemStatusOfPreparation selectedItemStatus)
    {
        return SelectedItemStatusOfPreparationEntity.builder()
                .id(selectedItemStatus.getId())
                .statusName(selectedItemStatus.getStatusName())
                .build();
    }
}

