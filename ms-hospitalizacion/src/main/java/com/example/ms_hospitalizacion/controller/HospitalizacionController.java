package com.example.ms_hospitalizacion.controller;

import com.example.ms_hospitalizacion.model.Hospitalizacion;
import com.example.ms_hospitalizacion.service.HospitalizacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospitalizaciones")
@RequiredArgsConstructor
public class HospitalizacionController {

    private final HospitalizacionService service;

    @GetMapping
    public ResponseEntity<List<Hospitalizacion>> getGlobal() {
        return ResponseEntity.ok(service.getGlobal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hospitalizacion> getPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPorId(id));
    }

    @PostMapping
    public ResponseEntity<Hospitalizacion> crearId(@Valid @RequestBody Hospitalizacion hospitalizacion) {
        Hospitalizacion nuevaHosp = service.crearId(hospitalizacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaHosp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarId(@PathVariable Long id) {
        service.eliminarId(id);
        return ResponseEntity.noContent().build();
    }
}