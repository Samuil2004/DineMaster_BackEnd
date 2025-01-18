package nl.fontys.s3.dinemasterbackend.domain.classes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Burger extends Item {
    private Integer weight;
}
