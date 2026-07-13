package com.example.ms_especialidades.service;

import com.example.ms_especialidades.model.Especialidad;
import com.example.ms_especialidades.repository.EspecialidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspecialidadService {

    private final EspecialidadRepository repository;

    public List<Especialidad> getGlobal() {
        return repository.findAll();
    }

    public Especialidad getPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("La especialidad con ID " + id + " no existe o fue eliminada logicamente."));
    }

    public Especialidad crearId(Especialidad especialidad) {
        return repository.save(especialidad);
    }

    public void eliminarId(Long id) {
        // Primero validamos que exista usando el metodo de arriba.
        // Si no existe, lanza el error. Si existe, la elimina logicamente.
        Especialidad especialidad = getPorId(id);
        repository.delete(especialidad);
    }
}