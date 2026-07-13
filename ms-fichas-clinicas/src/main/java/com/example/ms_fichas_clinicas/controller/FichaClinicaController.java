package com.example.ms_fichas_clinicas.controller;

import com.example.ms_fichas_clinicas.model.FichaClinica;
import com.example.ms_fichas_clinicas.service.FichaClinicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fichas")
@RequiredArgsConstructor
public class FichaClinicaController {

    private final FichaClinicaService service;

    @GetMapping
    public ResponseEntity<List<FichaClinica>> getGlobal() {
        return ResponseEntity.ok(service.getGlobal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FichaClinica> getPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPorId(id));
    }

    @PostMapping
    public ResponseEntity<FichaClinica> crearId(@Valid @RequestBody FichaClinica ficha) {
        FichaClinica nuevaFicha = service.crearId(ficha);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaFicha);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarId(@PathVariable Long id) {
        service.eliminarId(id);
        return ResponseEntity.noContent().build();
    }
}