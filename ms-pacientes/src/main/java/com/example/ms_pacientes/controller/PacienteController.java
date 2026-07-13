package com.example.ms_pacientes.controller;

import com.example.ms_pacientes.model.Paciente;
import com.example.ms_pacientes.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService service;

    @GetMapping
    public ResponseEntity<List<Paciente>> getGlobal() {
        return ResponseEntity.ok(service.getGlobal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> getPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPorId(id));
    }

    @PostMapping
    public ResponseEntity<Paciente> crearId(@Valid @RequestBody Paciente paciente) {
        Paciente nuevoPaciente = service.crearId(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPaciente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> modificarId(@PathVariable Long id, @Valid @RequestBody Paciente paciente) {
        return ResponseEntity.ok(service.modificarId(id, paciente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarId(@PathVariable Long id) {
        service.eliminarId(id);
        return ResponseEntity.noContent().build();
    }
}