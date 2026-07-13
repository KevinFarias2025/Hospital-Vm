package com.example.ms_urgencias.service;

import com.example.ms_urgencias.client.MedicoClient;
import com.example.ms_urgencias.client.PacienteClient;
import com.example.ms_urgencias.model.Urgencia;
import com.example.ms_urgencias.repository.UrgenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UrgenciaService {

    private final UrgenciaRepository repository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;

    public List<Urgencia> getGlobal() {
        return repository.findAll();
    }

    public Urgencia getPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("La urgencia con ID " + id + " no existe o fue eliminada logicamente."));
    }

    public Urgencia crearId(Urgencia urgencia) {
        try {
            var paciente = pacienteClient.obtenerPacientePorId(urgencia.getId_paciente());
            if (paciente == null) throw new RuntimeException("Paciente no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error validando paciente: " + e.getMessage());
        }

        try {
            var medico = medicoClient.obtenerMedicoPorId(urgencia.getId_medico());
            if (medico == null) throw new RuntimeException("Medico no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error validando medico: " + e.getMessage());
        }

        return repository.save(urgencia);
    }

    public void eliminarId(Long id) {
        Urgencia urgencia = getPorId(id);
        repository.delete(urgencia);
    }
}