package nl.fontys.s3.dinemasterbackend;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootApplication
public class DineMasterBackEndApplication {

    @Autowired
    private DataSource dataSource;


    public static void main(String[] args) {
        SpringApplication.run(DineMasterBackEndApplication.class, args);
    }

    @PostConstruct
    public void logDatabaseUrl() {
        try {
            String url = dataSource.getConnection().getMetaData().getURL();
            System.out.println("Connected to the database: " + url);
        } catch (SQLException e) {
            System.err.println("Error getting database connection URL: " + e.getMessage());
        }
    }
}
