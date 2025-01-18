package nl.fontys.s3.dinemasterbackend.domain.classes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SelectedItemStatusOfPreparation {
    private Long id;
    private String statusName;
}
