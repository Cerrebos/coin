package co.in;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class MainApplication extends JFrame {

	static ConfigurableApplicationContext ctx;

	public static void main(String[] args) {
		ctx = new SpringApplicationBuilder(MainApplication.class).headless(false).run(args);
	}

}
