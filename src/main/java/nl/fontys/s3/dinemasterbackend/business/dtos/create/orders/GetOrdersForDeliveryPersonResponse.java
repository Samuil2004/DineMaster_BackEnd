package nl.fontys.s3.dinemasterbackend.business.dtos.create.orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nl.fontys.s3.dinemasterbackend.domain.classes.Order;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class GetOrdersForDeliveryPersonResponse {
    private List<Order> allOrdersAssignedToToADeliveryPerson;
    private Long totalCount;
}
