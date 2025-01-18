package nl.fontys.s3.dinemasterbackend.business.order_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.get.CalculateDeliveryFeeResponse;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.ValidateAddressRequest;

public interface CalculateDeliveryFee {
    CalculateDeliveryFeeResponse calculateDeliveryFee(ValidateAddressRequest request);
}
