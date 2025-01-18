package nl.fontys.s3.dinemasterbackend.business.dtos.get;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CalculateDeliveryFeeResponse {
    private Double deliveryFee;
}
