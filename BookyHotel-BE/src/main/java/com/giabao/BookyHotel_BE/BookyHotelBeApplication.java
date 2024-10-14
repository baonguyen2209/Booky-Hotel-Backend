package com.giabao.BookyHotel_BE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class BookyHotelBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookyHotelBeApplication.class, args);
	}

}
