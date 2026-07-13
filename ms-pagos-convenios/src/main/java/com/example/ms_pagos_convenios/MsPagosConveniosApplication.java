package com.example.ms_pagos_convenios;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Gateway Server")})
public class MsPagosConveniosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPagosConveniosApplication.class, args);
	}

}
