package com.example.ms_recetas.controller;

import com.example.ms_recetas.model.Receta;
import com.example.ms_recetas.service.RecetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
@RequiredArgsConstructor
public class RecetaController {

    private final RecetaService service;

    @GetMapping
    public ResponseEntity<List<Receta>> getGlobal() {
        return ResponseEntity.ok(service.getGlobal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Receta> getPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPorId(id));
    }

    @PostMapping
    public ResponseEntity<Receta> crearId(@Valid @RequestBody Receta receta) {
        Receta nuevaReceta = service.crearId(receta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReceta);
    }

    // TAREA 1: Endpoint PUT para modificar la receta médica
    @PutMapping("/{id}")
    public ResponseEntity<Receta> modificarId(@PathVariable Long id, @Valid @RequestBody Receta receta) {
        return ResponseEntity.ok(service.modificarId(id, receta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarId(@PathVariable Long id) {
        service.eliminarId(id);
        return ResponseEntity.noContent().build();
    }
}