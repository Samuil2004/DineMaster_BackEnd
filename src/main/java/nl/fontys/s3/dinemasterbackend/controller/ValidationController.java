package nl.fontys.s3.dinemasterbackend.controller;

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
    public ResponseEntity<String> testPublicApi(@RequestBody @Valid ValidateAddressRequest request ) {
        String fullAddress = String.format("%s, %s, %s, %s",
                request.getStreet(), request.getCity(), request.getPostalCode(), request.getCountry());
        String nominatimURL = String.format("https://nominatim.openstreetmap.org/search?q=%s&format=json&addressdetails=1",
                UriUtils.encode(fullAddress, StandardCharsets.UTF_8));

        try {
            URL url = new URL(nominatimURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


            JSONArray jsonArray = new JSONArray(response.toString());
            if (jsonArray.length() > 0) {
                String lat = jsonArray.getJSONObject(0).getString("lat");
                String lon = jsonArray.getJSONObject(0).getString("lon");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("TRUE");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("False");
        }
         catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}
