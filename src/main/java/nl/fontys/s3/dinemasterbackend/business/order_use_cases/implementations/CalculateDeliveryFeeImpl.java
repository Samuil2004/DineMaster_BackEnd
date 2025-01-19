package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.CalculateDeliveryFeeResponse;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.ValidateAddressRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.ValidateAddressResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.OperationNotPossible;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.CalculateDeliveryFee;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.CalculateDistance;
import nl.fontys.s3.dinemasterbackend.business.validation_services.ValidateAddress;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Service
@AllArgsConstructor
public class CalculateDeliveryFeeImpl implements CalculateDeliveryFee {
    private final ValidateAddress validateAddress;
    private final CalculateDistance calculateDistance;


    private static final double STARTING_FEE = 0.99;
    private static final double MAX_DELIVERY_FEE = 11.00;
    private static final double BASE_FEE_PER_KM = 0.35;
    private static final double EVENING_SURCHARGE = 1.00;
    private static final double WEEKEND_SURCHARGE = 1.50;

    @Override
    public CalculateDeliveryFeeResponse calculateDeliveryFee(ValidateAddressRequest request) {
        ValidateAddressResponse validatedAddress = validateAddress.isAddressValid(request);
        if (Boolean.TRUE.equals(validatedAddress.getIsValid())) {
            Map<String, Double> durationAndDistanceOfDelivery = calculateDistance.calculateDistanceBetweenRestaurantAndDeliveryPoint(validatedAddress.getLocationLatitude(), validatedAddress.getLocationLongitude());
            Double distance = durationAndDistanceOfDelivery.get("distance");

            double fee = STARTING_FEE + BASE_FEE_PER_KM * distance;

            if (isEvening(LocalTime.now())) {
                fee += EVENING_SURCHARGE;
            }
            if (isWeekend(LocalDate.now().getDayOfWeek())) {
                fee += WEEKEND_SURCHARGE;
            }

            return CalculateDeliveryFeeResponse.builder().deliveryFee(fee).build();
        } else {
            throw new OperationNotPossible("Address is not valid");
        }

    }

    private boolean isEvening(LocalTime time) {
        // Evening hours : between 18:00 and 23:59
        return time.isAfter(LocalTime.of(18, 0)) && time.isBefore(LocalTime.of(23, 59));
    }

    private boolean isWeekend(DayOfWeek dayOfWeek) {
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
