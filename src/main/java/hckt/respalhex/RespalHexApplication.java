package hckt.respalhex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("hckt.respalhex.**.config")
public class RespalHexApplication {
    public static void main(String[] args) {
        SpringApplication.run(RespalHexApplication.class, args);
    }
}
