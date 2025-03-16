package vn.fptu.reasbe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 *
 * @author dungnguyen
 */
@Configuration
@EnableJpaRepositories(basePackages = "vn.fptu.reasbe.repository")
@EnableMongoRepositories(basePackages = "vn.fptu.reasbe.repository.mongodb")
public class DatabaseConfig {
}