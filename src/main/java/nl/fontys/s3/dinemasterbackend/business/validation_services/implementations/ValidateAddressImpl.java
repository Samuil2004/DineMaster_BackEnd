package nl.fontys.s3.dinemasterbackend.business.validation_services.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.ValidateAddressRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.ValidateAddressResponse;
import nl.fontys.s3.dinemasterbackend.business.validation_services.ValidateAddress;
import org.cloudinary.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
public class ValidateAddressImpl implements ValidateAddress {


    @Override
    public ValidateAddressResponse isAddressValid(ValidateAddressRequest request) {
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
                return ValidateAddressResponse.builder().isValid(true).locationLatitude(lat).locationLongitude(lon).build();
            }
            return ValidateAddressResponse.builder().isValid(false).build();
        } catch (Exception e) {
            return ValidateAddressResponse.builder().isValid(false).build();
        }
    }
}
