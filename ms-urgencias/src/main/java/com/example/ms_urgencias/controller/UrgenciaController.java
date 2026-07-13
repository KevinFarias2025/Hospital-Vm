package com.example.ms_urgencias.controller;

import com.example.ms_urgencias.model.Urgencia;
import com.example.ms_urgencias.service.UrgenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/urgencias")
@RequiredArgsConstructor
public class UrgenciaController {

    private final UrgenciaService service;

    @GetMapping
    public ResponseEntity<List<Urgencia>> getGlobal() {
        return ResponseEntity.ok(service.getGlobal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Urgencia> getPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPorId(id));
    }

    @PostMapping
    public ResponseEntity<Urgencia> crearId(@Valid @RequestBody Urgencia urgencia) {
        Urgencia nuevaUrgencia = service.crearId(urgencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaUrgencia);
    }

    // TAREA 1: Método PUT añadido aquí
    @PutMapping("/{id}")
    public ResponseEntity<Urgencia> modificarId(@PathVariable Long id, @Valid @RequestBody Urgencia urgencia) {
        return ResponseEntity.ok(service.modificarId(id, urgencia));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarId(@PathVariable Long id) {
        service.eliminarId(id);
        return ResponseEntity.noContent().build();
    }
}