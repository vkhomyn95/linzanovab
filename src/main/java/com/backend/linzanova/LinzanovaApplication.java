package com.backend.linzanova;

import com.backend.linzanova.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class LinzanovaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinzanovaApplication.class, args);
	}

}
