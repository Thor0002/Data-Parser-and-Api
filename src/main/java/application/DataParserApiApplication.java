package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DataParserApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataParserApiApplication.class, args);
    }
}
