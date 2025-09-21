package com.gameworkshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gameworkshop.infrastructure.persistence.mybatis.mapper")
public class GameWorkshopServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameWorkshopServiceApplication.class, args);
	}

}
