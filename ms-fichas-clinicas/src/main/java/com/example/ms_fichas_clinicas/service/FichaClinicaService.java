package com.example.ms_fichas_clinicas.service;

import com.example.ms_fichas_clinicas.client.PacienteClient;
import com.example.ms_fichas_clinicas.model.FichaClinica;
import com.example.ms_fichas_clinicas.repository.FichaClinicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FichaClinicaService {

    private final FichaClinicaRepository repository;
    private final PacienteClient pacienteClient;

    public List<FichaClinica> getGlobal() {
        return repository.findAll();
    }

    public FichaClinica getPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("La ficha clinica con ID " + id + " no existe o fue eliminada."));
    }

    public FichaClinica crearId(FichaClinica ficha) {
        try {
            var paciente = pacienteClient.obtenerPacientePorId(ficha.getId_paciente());
            if (paciente == null) throw new RuntimeException("Paciente no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error al validar paciente: " + e.getMessage());
        }

        return repository.save(ficha);
    }

    public void eliminarId(Long id) {
        FichaClinica ficha = getPorId(id);
        repository.delete(ficha);
    }
}