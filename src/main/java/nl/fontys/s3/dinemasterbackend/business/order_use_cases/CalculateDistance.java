package nl.fontys.s3.dinemasterbackend.business.order_use_cases;

import java.util.Map;

public interface CalculateDistance {
    Map<String,Double> calculateDistanceBetweenRestaurantAndDeliveryPoint(String deliveryPointLongitude, String deliveryPointLatitude);
}
