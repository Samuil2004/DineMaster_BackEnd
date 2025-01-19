package nl.fontys.s3.dinemasterbackend.controller;

import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageForwardRequest;
import com.opencagedata.jopencage.model.JOpenCageLatLng;
import com.opencagedata.jopencage.model.JOpenCageResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.CalculateDeliveryFeeResponse;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.ValidateAddressRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.ValidateAddressResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.OperationNotPossible;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.CalculateDeliveryFee;
import nl.fontys.s3.dinemasterbackend.business.validation_services.ValidateAddress;
import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/validation")
@AllArgsConstructor
public class ValidationController {

    private final ValidateAddress validateAddress;
    private final CalculateDeliveryFee calculateDeliveryFee;

    //As a CUSTOMER I should have access to the algorithm that validates the address
    @RolesAllowed({"CUSTOMER"})
    @PostMapping("/address")
    public ResponseEntity<ValidateAddressResponse> validateAddress(@RequestBody @Valid ValidateAddressRequest validateAddressRequest) {
        ValidateAddressResponse response = validateAddress.isAddressValid(validateAddressRequest);
        return ResponseEntity.ok(response);
    }

    //As a CUSTOMER I should have access to the algorithm that calculates the delivery fee
    @RolesAllowed({"CUSTOMER"})
    @PostMapping("/delivery-fee")
    public ResponseEntity<CalculateDeliveryFeeResponse> calculateDeliveryFeeBasedOnProvidedEndAddress(@RequestBody @Valid ValidateAddressRequest validateAddressRequest) {
        CalculateDeliveryFeeResponse response = calculateDeliveryFee.calculateDeliveryFee(validateAddressRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/test-public-api")
    public ResponseEntity<String> testPublicApi(@RequestBody @Valid ValidateAddressRequest validationRequest ) {

        String address = validationRequest.getStreet() + ", "
                + validationRequest.getCity() + ", "
                + validationRequest.getPostalCode() + ", "
                + validationRequest.getCountry();

        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("bbe24e79785a4c44b59bf417e2174cb0");
        JOpenCageForwardRequest request = new JOpenCageForwardRequest(address);
        //request.setRestrictToCountryCode("za"); // restrict results to a specific country
        //request.setBounds(18.367, -34.109, 18.770, -33.704); // restrict results to a geographic bounding box (southWestLng, southWestLat, northEastLng, northEastLat)

        JOpenCageResponse response = jOpenCageGeocoder.forward(request);
        JOpenCageLatLng firstResultLatLng = response.getFirstPosition(); // get the coordinate pair of the first result
        System.out.println(firstResultLatLng.getLat().toString() + "," + firstResultLatLng.getLng().toString());

        String coordinates = firstResultLatLng.getLat().toString() + "," + firstResultLatLng.getLng().toString();

                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Coordinate"+ coordinates);
    }


    @PostMapping("/test-public-api-calc-del-fee")
    public ResponseEntity<String> testDeliveryFee(@RequestParam(value = "deliveryPointLatitude", required = true) String deliveryPointLatitude,
                                                  @RequestParam(value = "deliveryPointLongitude", required = true) String deliveryPointLongitude) {

        String deliveryPointCoordinates = deliveryPointLatitude + "," + deliveryPointLongitude;
        String RESTAURANT_COORDINATES = "51.437446,5.479871";
        String apiKey = "c224ce090d2945dabe2114447a556c83";

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("array: " + featuresArray);
//
//            if (featuresArray.length() > 0) {
//                JSONObject properties = featuresArray.getJSONObject(0).getJSONObject("properties");
//
//                double distanceInMeters = properties.getDouble("distance");
//                double durationInSeconds = properties.getDouble("time");
//
//                double distanceInKm = distanceInMeters / 1000.0;
//                double durationInMinutes = durationInSeconds / 60.0;
//
//                if (distanceInKm > 15) {
//                    throw new OperationNotPossible("Provided address is out of delivery range");
//                }
//
//                resultMap.put("distance", distanceInKm);
//                resultMap.put("duration", durationInMinutes);
//                String results = distanceInKm + ", "
//                        + durationInMinutes;
//
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fouund: " + results);
//
//            }
//
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No address found");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }





    }

}
