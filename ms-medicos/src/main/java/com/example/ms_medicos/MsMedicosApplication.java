package com.example.ms_medicos;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Gateway Server")})
public class MsMedicosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsMedicosApplication.class, args);
	}

}
