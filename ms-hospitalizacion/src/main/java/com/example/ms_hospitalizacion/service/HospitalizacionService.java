package com.example.ms_hospitalizacion.service;

import com.example.ms_hospitalizacion.client.MedicoClient;
import com.example.ms_hospitalizacion.client.PacienteClient;
import com.example.ms_hospitalizacion.model.Hospitalizacion;
import com.example.ms_hospitalizacion.repository.HospitalizacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalizacionService {

    private final HospitalizacionRepository repository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;

    public List<Hospitalizacion> getGlobal() {
        return repository.findAll();
    }

    public Hospitalizacion getPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("La hospitalizacion con ID " + id + " no existe o fue eliminada."));
    }

    public Hospitalizacion crearId(Hospitalizacion hospitalizacion) {
        try {
            var paciente = pacienteClient.obtenerPacientePorId(hospitalizacion.getId_paciente());
            if (paciente == null) throw new RuntimeException("Paciente no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error al validar paciente: " + e.getMessage());
        }

        try {
            var medico = medicoClient.obtenerMedicoPorId(hospitalizacion.getId_medico());
            if (medico == null) throw new RuntimeException("Medico no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error al validar medico: " + e.getMessage());
        }

        return repository.save(hospitalizacion);
    }

    public void eliminarId(Long id) {
        Hospitalizacion hospitalizacion = getPorId(id);
        repository.delete(hospitalizacion);
    }
}