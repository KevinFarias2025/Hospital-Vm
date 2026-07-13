package com.example.ms_medicos.client;

import com.example.ms_medicos.dto.EspecialidadDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "ms-especialidades")
public interface EspecialidadClient {

    @GetMapping("/api/especialidades/{id}")
    EspecialidadDTO obtenerEspecialidadPorId(@PathVariable("id") Long id);
}