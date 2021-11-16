package co.in;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MainApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);
	}
}
