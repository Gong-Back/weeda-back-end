package gongback.weeda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class, MongoReactiveAutoConfiguration.class})
public class WeedaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeedaApplication.class, args);
	}

}
