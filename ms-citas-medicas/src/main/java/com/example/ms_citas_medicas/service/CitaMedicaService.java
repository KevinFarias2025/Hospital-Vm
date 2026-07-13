package com.example.ms_citas_medicas.service;

import com.example.ms_citas_medicas.client.MedicoClient;
import com.example.ms_citas_medicas.client.PacienteClient;
import com.example.ms_citas_medicas.client.PagoClient;
import com.example.ms_citas_medicas.model.CitaMedica;
import com.example.ms_citas_medicas.repository.CitaMedicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CitaMedicaService {

    private final CitaMedicaRepository repository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;
    private final PagoClient pagoClient; // Inyección de pagos

    public List<CitaMedica> getGlobal() {
        return repository.findAll();
    }

    public CitaMedica getPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("La cita médica con ID " + id + " no existe o fue eliminada lógicamente."));
    }

    public CitaMedica crearId(CitaMedica cita) {
        // Validar Paciente
        try {
            var paciente = pacienteClient.obtenerPacientePorId(cita.getId_paciente());
            if (paciente == null) throw new RuntimeException("Paciente no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error al validar paciente: " + e.getMessage());
        }

        // Validar Médico
        try {
            var medico = medicoClient.obtenerMedicoPorId(cita.getId_medico());
            if (medico == null) throw new RuntimeException("Médico no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error al validar médico: " + e.getMessage());
        }

        // Guardamos la cita médica
        CitaMedica citaGuardada = repository.save(cita);

        // TAREA 3: Cobro automático a pagos por $25.000
        try {
            Map<String, Object> requestCobro = new HashMap<>();
            requestCobro.put("id_paciente", citaGuardada.getId_paciente());
            requestCobro.put("id_referencia_servicio", citaGuardada.getId_cita());
            requestCobro.put("tipo_servicio", "CONSULTA");
            requestCobro.put("monto_base", 25000.0);

            pagoClient.procesarCobro(requestCobro);
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudo procesar el cobro de la Cita Médica - " + e.getMessage());
        }

        return citaGuardada;
    }

    // TAREA 1: Método PUT para Modificar
    public CitaMedica modificarId(Long id, CitaMedica citaActualizada) {
        CitaMedica citaExistente = getPorId(id);

        // Validar paciente solo si cambió
        if (!citaExistente.getId_paciente().equals(citaActualizada.getId_paciente())) {
            try {
                var paciente = pacienteClient.obtenerPacientePorId(citaActualizada.getId_paciente());
                if (paciente == null) throw new RuntimeException("Paciente no existe.");
            } catch (Exception e) {
                throw new RuntimeException("Error validando paciente: " + e.getMessage());
            }
        }

        // Validar médico solo si cambió
        if (!citaExistente.getId_medico().equals(citaActualizada.getId_medico())) {
            try {
                var medico = medicoClient.obtenerMedicoPorId(citaActualizada.getId_medico());
                if (medico == null) throw new RuntimeException("Médico no existe.");
            } catch (Exception e) {
                throw new RuntimeException("Error validando médico: " + e.getMessage());
            }
        }

        // Actualizamos los campos según el modelo
        citaExistente.setId_paciente(citaActualizada.getId_paciente());
        citaExistente.setId_medico(citaActualizada.getId_medico());
        citaExistente.setFecha_hora(citaActualizada.getFecha_hora());
        citaExistente.setEstado_cita(citaActualizada.getEstado_cita());

        return repository.save(citaExistente);
    }

    public void eliminarId(Long id) {
        CitaMedica cita = getPorId(id);
        repository.delete(cita);
    }
}