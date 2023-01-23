package gongback.weeda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "gongback.weeda.common.properties")
public class WeedaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeedaApplication.class, args);
    }

}
