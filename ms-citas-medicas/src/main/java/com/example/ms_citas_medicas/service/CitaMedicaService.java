package com.example.ms_citas_medicas.service;

import com.example.ms_citas_medicas.client.MedicoClient;
import com.example.ms_citas_medicas.client.PacienteClient;
import com.example.ms_citas_medicas.model.CitaMedica;
import com.example.ms_citas_medicas.repository.CitaMedicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaMedicaService {

    private final CitaMedicaRepository repository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;

    public List<CitaMedica> getGlobal() {
        return repository.findAll();
    }

    public CitaMedica getPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("La cita con ID " + id + " no existe o fue cancelada logicamente."));
    }

    public CitaMedica crearId(CitaMedica cita) {
        try {
            var paciente = pacienteClient.obtenerPacientePorId(cita.getId_paciente());
            if (paciente == null) throw new RuntimeException("Paciente no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error validando paciente: " + e.getMessage());
        }

        try {
            var medico = medicoClient.obtenerMedicoPorId(cita.getId_medico());
            if (medico == null) throw new RuntimeException("Medico no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error validando medico: " + e.getMessage());
        }

        return repository.save(cita);
    }

    public CitaMedica modificarId(Long id, CitaMedica citaActualizada) {
        CitaMedica citaExistente = getPorId(id);

        // Validar paciente si el id_paciente enviado es distinto al actual
        if (!citaExistente.getId_paciente().equals(citaActualizada.getId_paciente())) {
            try {
                var paciente = pacienteClient.obtenerPacientePorId(citaActualizada.getId_paciente());
                if (paciente == null) throw new RuntimeException("Paciente no existe.");
            } catch (Exception e) {
                throw new RuntimeException("Error validando paciente: " + e.getMessage());
            }
        }

        // Validar medico si el id_medico enviado es distinto al actual
        if (!citaExistente.getId_medico().equals(citaActualizada.getId_medico())) {
            try {
                var medico = medicoClient.obtenerMedicoPorId(citaActualizada.getId_medico());
                if (medico == null) throw new RuntimeException("Medico no existe.");
            } catch (Exception e) {
                throw new RuntimeException("Error validando medico: " + e.getMessage());
            }
        }

        citaExistente.setId_paciente(citaActualizada.getId_paciente());
        citaExistente.setId_medico(citaActualizada.getId_medico());
        citaExistente.setFecha_hora(citaActualizada.getFecha_hora());
        citaExistente.setEstado_cita(citaActualizada.getEstado_cita());
        citaExistente.setEstado_activo(citaActualizada.isEstado_activo());

        return repository.save(citaExistente);
    }

    public void eliminarId(Long id) {
        CitaMedica cita = getPorId(id);
        repository.delete(cita);
    }
}