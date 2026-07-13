package com.example.ms_citas_medicas.controller;

import com.example.ms_citas_medicas.model.CitaMedica;
import com.example.ms_citas_medicas.service.CitaMedicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas-medicas")
@RequiredArgsConstructor
public class CitaMedicaController {

    private final CitaMedicaService service;

    @GetMapping
    public ResponseEntity<List<CitaMedica>> getGlobal() {
        return ResponseEntity.ok(service.getGlobal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaMedica> getPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPorId(id));
    }

    @PostMapping
    public ResponseEntity<CitaMedica> crearId(@Valid @RequestBody CitaMedica cita) {
        CitaMedica nuevaCita = service.crearId(cita);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
    }

    // TAREA 1: Endpoint PUT para modificar la cita médica
    @PutMapping("/{id}")
    public ResponseEntity<CitaMedica> modificarId(@PathVariable Long id, @Valid @RequestBody CitaMedica cita) {
        return ResponseEntity.ok(service.modificarId(id, cita));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarId(@PathVariable Long id) {
        service.eliminarId(id);
        return ResponseEntity.noContent().build();
    }
}