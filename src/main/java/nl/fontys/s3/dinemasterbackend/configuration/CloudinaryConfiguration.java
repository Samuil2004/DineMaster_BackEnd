package nl.fontys.s3.dinemasterbackend.configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfiguration {

//    @Value("${cloudinary.cloudName}")
//    private String cloudName;
//
//    @Value("${cloudinary.apiKey}")
//    private String apiKey;
//
//    @Value("${cloudinary.apiKeySecret}")
//    private String apiSecret;


    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dcwcvn160",
                "api_key", "782148399297396",
                "api_secret", "MugNAK-ft8vIdO-QNWUPKTZxoQ8"));
    }
}
