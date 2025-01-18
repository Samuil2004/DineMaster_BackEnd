package nl.fontys.s3.dinemasterbackend.domain.classes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class Pizza extends Item {
    private List<PizzaSize> sizes;
    private String base;
}
