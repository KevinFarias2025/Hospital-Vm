package com.example.ms_examenes.service;

import com.example.ms_examenes.client.MedicoClient;
import com.example.ms_examenes.client.PacienteClient;
import com.example.ms_examenes.model.Examen;
import com.example.ms_examenes.repository.ExamenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamenService {

    private final ExamenRepository repository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;

    public List<Examen> getGlobal() {
        return repository.findAll();
    }

    public Examen getPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("El examen con ID " + id + " no existe o fue eliminado."));
    }

    public Examen crearId(Examen examen) {
        try {
            var paciente = pacienteClient.obtenerPacientePorId(examen.getId_paciente());
            if (paciente == null) throw new RuntimeException("Paciente no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error validando paciente: " + e.getMessage());
        }

        try {
            var medico = medicoClient.obtenerMedicoPorId(examen.getId_medico());
            if (medico == null) throw new RuntimeException("Medico no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error validando medico: " + e.getMessage());
        }

        return repository.save(examen);
    }

    public void eliminarId(Long id) {
        Examen examen = getPorId(id);
        repository.delete(examen);
    }
}