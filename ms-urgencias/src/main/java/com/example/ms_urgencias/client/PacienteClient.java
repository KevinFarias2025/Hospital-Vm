package com.example.ms_urgencias.client;
import com.example.ms_urgencias.dto.PacienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-pacientes")
public interface PacienteClient {
    @GetMapping("/api/pacientes/{id}")
    PacienteDTO obtenerPacientePorId(@PathVariable("id") Long id);
}