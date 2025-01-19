package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import nl.fontys.s3.dinemasterbackend.business.exceptions.OperationNotPossible;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.CalculateDistance;
import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalculateDistanceImpl implements CalculateDistance {

    private static final String RESTAURANT_COORDINATES = "51.437446,5.479871";
    private final String apiKey;

    public CalculateDistanceImpl(@Value("${geoapify.key}") String apiKey) {
        this.apiKey = apiKey;
    }


    @Override
    public Map<String, Double> calculateDistanceBetweenRestaurantAndDeliveryPoint(String deliveryPointLatitude, String deliveryPointLongitude) {
        //String deliveryPointCoordinates = deliveryPointLongitude + "," + deliveryPointLatitude;
        String deliveryPointCoordinates = deliveryPointLatitude + "," + deliveryPointLongitude;

        String apiUrl = String.format(
                "https://api.geoapify.com/v1/routing?waypoints=%s|%s&mode=drive&apiKey=%s",
                RESTAURANT_COORDINATES, deliveryPointCoordinates, apiKey
        );

        Map<String, Double> resultMap = new HashMap<>();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder jsonResponse = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                jsonResponse.append(inputLine);
            }
            in.close();

            JSONObject responseObject = new JSONObject(jsonResponse.toString());

            JSONArray featuresArray = responseObject.getJSONArray("features");
            if (featuresArray.length() > 0) {
                JSONObject properties = featuresArray.getJSONObject(0).getJSONObject("properties");

                double distanceInMeters = properties.getDouble("distance");
                double durationInSeconds = properties.getDouble("time");

                double distanceInKm = distanceInMeters / 1000.0;
                double durationInMinutes = durationInSeconds / 60.0;

                if (distanceInKm > 15) {
                    throw new OperationNotPossible("Provided address is out of delivery range");
                }

                resultMap.put("distance", distanceInKm);
                resultMap.put("duration", durationInMinutes);
            }

            return resultMap;
        } catch (Exception e) {
            throw new OperationNotPossible("RETRIEVING DIRECTIONS FAILED");
        }
    }
}
