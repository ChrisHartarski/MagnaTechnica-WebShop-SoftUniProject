package bg.magna.websop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MagnaTechnicaWebShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagnaTechnicaWebShopApplication.class, args);
	}

}
