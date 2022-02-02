package com.ee.cart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CartApplication implements CommandLineRunner {
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger();

	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(CartApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}