package nl.fontys.s3.dinemasterbackend.domain.classes;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class ItemCategory {
    private Long categoryId;
    private String categoryName;
}
