package nl.fontys.s3.dinemasterbackend.domain.classes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class Soup extends Item {
    private Boolean isVegetarian;
}
