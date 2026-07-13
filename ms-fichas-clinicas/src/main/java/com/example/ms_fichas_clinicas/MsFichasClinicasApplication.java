package com.example.ms_fichas_clinicas;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Gateway Server")})
public class MsFichasClinicasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsFichasClinicasApplication.class, args);
	}
}