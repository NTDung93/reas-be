package vn.fptu.reasbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableAsync
public class ReasBeApplication {


    @Bean
    public ConcurrentMapCacheManager cacheManager() {
        return new ConcurrentMapCacheManager("otpCache");
    }

	public static void main(String[] args) {
		SpringApplication.run(ReasBeApplication.class, args);
	}
}
