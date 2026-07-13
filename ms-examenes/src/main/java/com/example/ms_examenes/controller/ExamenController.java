package com.example.ms_examenes.controller;

import com.example.ms_examenes.model.Examen;
import com.example.ms_examenes.service.ExamenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/examenes")
@RequiredArgsConstructor
public class ExamenController {

    private final ExamenService service;

    @GetMapping
    public ResponseEntity<List<Examen>> getGlobal() {
        return ResponseEntity.ok(service.getGlobal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Examen> getPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPorId(id));
    }

    @PostMapping
    public ResponseEntity<Examen> crearId(@Valid @RequestBody Examen examen) {
        Examen nuevoExamen = service.crearId(examen);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoExamen);
    }

    // TAREA 1: Método PUT añadido aquí
    @PutMapping("/{id}")
    public ResponseEntity<Examen> modificarId(@PathVariable Long id, @Valid @RequestBody Examen examen) {
        return ResponseEntity.ok(service.modificarId(id, examen));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarId(@PathVariable Long id) {
        service.eliminarId(id);
        return ResponseEntity.noContent().build();
    }
}