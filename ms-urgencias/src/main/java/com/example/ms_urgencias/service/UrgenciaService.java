package com.example.ms_urgencias.service;

import com.example.ms_urgencias.client.MedicoClient;
import com.example.ms_urgencias.client.PacienteClient;
import com.example.ms_urgencias.client.PagoClient;
import com.example.ms_urgencias.model.Urgencia;
import com.example.ms_urgencias.repository.UrgenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UrgenciaService {

    private final UrgenciaRepository repository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;
    private final PagoClient pagoClient; // Inyección para pagos

    public List<Urgencia> getGlobal() {
        return repository.findAll();
    }

    public Urgencia getPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("La urgencia con ID " + id + " no existe o fue eliminada lógicamente."));
    }

    public Urgencia crearId(Urgencia urgencia) {
        // Validar Paciente
        try {
            var paciente = pacienteClient.obtenerPacientePorId(urgencia.getId_paciente());
            if (paciente == null) throw new RuntimeException("Paciente no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error validando paciente: " + e.getMessage());
        }

        // Validar Médico
        try {
            var medico = medicoClient.obtenerMedicoPorId(urgencia.getId_medico());
            if (medico == null) throw new RuntimeException("Médico no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error validando médico: " + e.getMessage());
        }

        // Guardamos la urgencia
        Urgencia urgenciaGuardada = repository.save(urgencia);

        // TAREA 3: Disparamos el cobro automático a Pagos por 50.000
        try {
            Map<String, Object> requestCobro = new HashMap<>();
            requestCobro.put("id_paciente", urgenciaGuardada.getId_paciente());
            requestCobro.put("id_referencia_servicio", urgenciaGuardada.getId_urgencia());
            requestCobro.put("tipo_servicio", "URGENCIA");
            requestCobro.put("monto_base", 50000.0);

            pagoClient.procesarCobro(requestCobro);
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudo procesar el cobro de Urgencia - " + e.getMessage());
        }

        return urgenciaGuardada;
    }

    // TAREA 1: Método PUT para Modificar
    public Urgencia modificarId(Long id, Urgencia urgenciaActualizada) {
        Urgencia urgenciaExistente = getPorId(id);

        // Validar paciente solo si cambió
        if (!urgenciaExistente.getId_paciente().equals(urgenciaActualizada.getId_paciente())) {
            try {
                var paciente = pacienteClient.obtenerPacientePorId(urgenciaActualizada.getId_paciente());
                if (paciente == null) throw new RuntimeException("Paciente no existe.");
            } catch (Exception e) {
                throw new RuntimeException("Error validando paciente: " + e.getMessage());
            }
        }

        // Validar médico solo si cambió
        if (!urgenciaExistente.getId_medico().equals(urgenciaActualizada.getId_medico())) {
            try {
                var medico = medicoClient.obtenerMedicoPorId(urgenciaActualizada.getId_medico());
                if (medico == null) throw new RuntimeException("Médico no existe.");
            } catch (Exception e) {
                throw new RuntimeException("Error validando médico: " + e.getMessage());
            }
        }

        // Actualizamos los campos basados en tu modelo
        urgenciaExistente.setId_paciente(urgenciaActualizada.getId_paciente());
        urgenciaExistente.setId_medico(urgenciaActualizada.getId_medico());
        urgenciaExistente.setMotivo(urgenciaActualizada.getMotivo());

        // ¡Aquí estaba el error! Se llama nivel_gravedad
        urgenciaExistente.setNivel_gravedad(urgenciaActualizada.getNivel_gravedad());

        return repository.save(urgenciaExistente);
    }

    public void eliminarId(Long id) {
        Urgencia urgencia = getPorId(id);
        repository.delete(urgencia);
    }
}