package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.update.orders.UpdateSelectedItemStatusRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.exceptions.OperationNotPossible;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.UpdateSelectedItemStatus;
import nl.fontys.s3.dinemasterbackend.persistence.entity.SelectedItemEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.SelectedItemStatusOfPreparationEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.SelectedItemEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.SelectedItemStatusOfPreparationEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateSelectedItemStatusImpl implements UpdateSelectedItemStatus {

    private final SelectedItemEntityRepository selectedItemEntityRepository;


    private final SelectedItemStatusOfPreparationEntityRepository selectedItemStatusOfPreparationEntityRepository;

    @Override
    @Transactional
    public void updateSelectedItemStatus(UpdateSelectedItemStatusRequest request) {

        Optional<SelectedItemEntity> foundById = selectedItemEntityRepository.findById(request.getSelectedItemId());
        if (foundById.isPresent()) {
            Optional<SelectedItemStatusOfPreparationEntity> foundStatus = selectedItemStatusOfPreparationEntityRepository.findByStatusName("PREPARED");
            if(foundStatus.isPresent()) {
                foundById.get().setStatusOfPreparation(foundStatus.get());
                selectedItemEntityRepository.save(foundById.get());
                return;
            }
            throw new OperationNotPossible("SELECTED STATUS NOT FOUND");
        }
        throw new NotFound("SELECTED ITEM NOT FOUND");
    }
}
