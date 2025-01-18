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
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.CalculateDeliveryFee;
import nl.fontys.s3.dinemasterbackend.business.validation_services.ValidateAddress;
import org.cloudinary.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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


//        String fullAddress = String.format("%s, %s, %s, %s",
//                request.getStreet(), request.getCity(), request.getPostalCode(), request.getCountry());
//        String nominatimURL = String.format("https://nominatim.openstreetmap.org/search?q=%s&format=json&addressdetails=1",
//                UriUtils.encode(fullAddress, StandardCharsets.UTF_8));
//
//        try {
//            URL url = new URL(nominatimURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("User-Agent", "DineMasterPro/1.0");
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String inputLine;
//            StringBuilder response = new StringBuilder();
//
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//
//
//            JSONArray jsonArray = new JSONArray(response.toString());
//            if (jsonArray.length() > 0) {
//                String lat = jsonArray.getJSONObject(0).getString("lat");
//                String lon = jsonArray.getJSONObject(0).getString("lon");
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("TRUE");
//            }
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("False");
//        }
//         catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
//        }

        //            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());


    }

}
