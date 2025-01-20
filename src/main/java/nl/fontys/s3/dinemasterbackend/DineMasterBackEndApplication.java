package nl.fontys.s3.dinemasterbackend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DineMasterBackEndApplication {

    public static void main(String[] args) {
        // Load environment variables from .env file
//        Dotenv dotenv = Dotenv.configure().load();
//
//        // Set variables as System properties for Spring Boot
//        System.setProperty("spring.datasource.url", dotenv.get("SPRING_DATASOURCE_URL"));
//        System.setProperty("spring.datasource.username", dotenv.get("SPRING_DATASOURCE_USERNAME"));
//        System.setProperty("spring.datasource.password", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
//        System.setProperty("jwt.secret", dotenv.get("JWT_SECRET"));
//        System.setProperty("geoapify.key", dotenv.get("GEOAPIFY_KEY"));
//        System.setProperty("openCage.key", dotenv.get("OPENCAGE_KEY"));
//        System.setProperty("spring.mail.username", dotenv.get("SPRING_MAIL_USERNAME"));
//        System.setProperty("spring.mail.password", dotenv.get("SPRING_MAIL_PASSWORD"));
//
//        System.setProperty("openCage.key", dotenv.get("OPENCAGE_KEY"));
//        System.setProperty("spring.mail.username", dotenv.get("SPRING_MAIL_USERNAME"));
//        System.setProperty("spring.mail.password", dotenv.get("SPRING_MAIL_PASSWORD"));
//
//        System.setProperty("cloudinary.cloudName", dotenv.get("CLOUDINARY_CLOUD_NAME"));
//        System.setProperty("cloudinary.apiKey", dotenv.get("CLOUDINARY_API_KEY"));
//        System.setProperty("cloudinary.apiKeySecret", dotenv.get("CLOUDINARY_API_SECRET"));

        SpringApplication.run(DineMasterBackEndApplication.class, args);
    }

}
