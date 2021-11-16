package co.in;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CoinApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(CoinApplication.class, args);

//		run.getBean(CoinService.class).generateEnrichedFlagData();

		//for standalone run in console, must pass param including Database open url + port :
//		String sessionId = args[0];
//		String ngrokUrl = args[1];
//		int x = Integer.parseInt(args[2]);
//		int y = Integer.parseInt(args[3]);

		run.getBean(CoinService.class).runDuckGeneration("kt06f3slakvqdeg8fvt5hwziu5lyq1fc", "ngrokUrl", 275, 72);

	}
}
