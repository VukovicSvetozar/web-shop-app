package org.vs;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.vs.service.SlikaService;

import jakarta.annotation.Resource;

@SpringBootApplication
public class WebshopApplication implements CommandLineRunner {

	@Resource
	SlikaService slikaService;

	public static void main(String[] args) {
		SpringApplication.run(WebshopApplication.class, args);
	}

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... arg) throws Exception {
		slikaService.init();
	}

}
