package com.be;

import com.be.config.ymlprop.RazorpayConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class CollocateTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CollocateTrackerApplication.class, args);
	}

}
