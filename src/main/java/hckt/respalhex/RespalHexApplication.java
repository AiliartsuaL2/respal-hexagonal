package hckt.respalhex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("hckt.respalhex.global.config")
public class RespalHexApplication {
    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }


    public static void main(String[] args) {
        SpringApplication.run(RespalHexApplication.class, args);
    }

}
