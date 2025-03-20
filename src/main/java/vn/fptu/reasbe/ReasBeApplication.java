package vn.fptu.reasbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReasBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReasBeApplication.class, args);
	}
}
