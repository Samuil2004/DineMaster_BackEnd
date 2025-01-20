package nl.fontys.s3.dinemasterbackend.business.validation_services.implementations;

import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageForwardRequest;
import com.opencagedata.jopencage.model.JOpenCageLatLng;
import com.opencagedata.jopencage.model.JOpenCageResponse;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.ValidateAddressRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.ValidateAddressResponse;
import nl.fontys.s3.dinemasterbackend.business.validation_services.ValidateAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ValidateAddressImpl implements ValidateAddress {
    private final String apiKey = "bbe24e79785a4c44b59bf417e2174cb0";
//    public ValidateAddressImpl(@Value("${openCage.key}") String apiKey) {
//        this.apiKey = apiKey;
//    }

    @Override
    public ValidateAddressResponse isAddressValid(ValidateAddressRequest validationRequest) {

        String address = validationRequest.getStreet() + ", "
                + validationRequest.getCity() + ", "
                + validationRequest.getPostalCode() + ", "
                + validationRequest.getCountry();

        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder(apiKey);
        JOpenCageForwardRequest request = new JOpenCageForwardRequest(address);
        //request.setRestrictToCountryCode("za"); // restrict results to a specific country
        //request.setBounds(18.367, -34.109, 18.770, -33.704); // restrict results to a geographic bounding box (southWestLng, southWestLat, northEastLng, northEastLat)

        JOpenCageResponse response = jOpenCageGeocoder.forward(request);
        JOpenCageLatLng firstResultLatLng = response.getFirstPosition(); // get the coordinate pair of the first result
        //System.out.println(firstResultLatLng.getLat().toString() + "," + firstResultLatLng.getLng().toString());
        if(firstResultLatLng != null) {
            return ValidateAddressResponse.builder().isValid(true).locationLatitude(firstResultLatLng.getLat().toString()).locationLongitude(firstResultLatLng.getLng().toString()).build();

        }
            return ValidateAddressResponse.builder().isValid(false).build();

        //String coordinates = firstResultLatLng.getLat().toString() + "," + firstResultLatLng.getLng().toString();

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
//                return ValidateAddressResponse.builder().isValid(true).locationLatitude(lat).locationLongitude(lon).build();
//            }
//            return ValidateAddressResponse.builder().isValid(false).build();
//        } catch (Exception e) {
//            return ValidateAddressResponse.builder().isValid(false).build();
//        }
    }
}
