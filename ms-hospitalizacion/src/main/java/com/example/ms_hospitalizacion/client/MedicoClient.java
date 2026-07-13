package com.example.ms_hospitalizacion.client;

import com.example.ms_hospitalizacion.dto.MedicoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-medicos")
public interface MedicoClient {

    @GetMapping("/api/medicos/{id}")
    MedicoDTO obtenerMedicoPorId(@PathVariable("id") Long id);
}