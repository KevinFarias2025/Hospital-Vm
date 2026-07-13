package com.example.ms_medicos.controller;

import com.example.ms_medicos.model.Medico;
import com.example.ms_medicos.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService service;

    @GetMapping
    public ResponseEntity<List<Medico>> getGlobal() {
        return ResponseEntity.ok(service.getGlobal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> getPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPorId(id));
    }

    @PostMapping
    public ResponseEntity<Medico> crearId(@Valid @RequestBody Medico medico) {
        Medico nuevoMedico = service.crearId(medico);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMedico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarId(@PathVariable Long id) {
        service.eliminarId(id);
        return ResponseEntity.noContent().build();
    }
}