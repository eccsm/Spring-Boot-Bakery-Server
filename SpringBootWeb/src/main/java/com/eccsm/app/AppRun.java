package com.eccsm.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.eccsm.property.FileStorageProperties;

@SpringBootApplication(scanBasePackages = "com.eccsm")
@EnableJpaRepositories("com.eccsm.repository")
@EntityScan("com.eccsm.model")
@EnableConfigurationProperties({FileStorageProperties.class})
public class AppRun  {


	public static void main(String[] args) {
		SpringApplication.run(AppRun.class,args);

	}

}
