package com.example.ms_hospitalizacion.service;

import com.example.ms_hospitalizacion.client.MedicoClient;
import com.example.ms_hospitalizacion.client.PacienteClient;
import com.example.ms_hospitalizacion.client.PagoClient;
import com.example.ms_hospitalizacion.model.Hospitalizacion;
import com.example.ms_hospitalizacion.repository.HospitalizacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HospitalizacionService {

    private final HospitalizacionRepository repository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;
    private final PagoClient pagoClient; // Inyección para pagos

    public List<Hospitalizacion> getGlobal() {
        return repository.findAll();
    }

    public Hospitalizacion getPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("La hospitalizacion con ID " + id + " no existe o fue eliminada."));
    }

    public Hospitalizacion crearId(Hospitalizacion hospitalizacion) {
        // Validar Paciente
        try {
            var paciente = pacienteClient.obtenerPacientePorId(hospitalizacion.getId_paciente());
            if (paciente == null) throw new RuntimeException("Paciente no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error al validar paciente: " + e.getMessage());
        }

        // Validar Médico
        try {
            var medico = medicoClient.obtenerMedicoPorId(hospitalizacion.getId_medico());
            if (medico == null) throw new RuntimeException("Medico no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error al validar medico: " + e.getMessage());
        }

        // Guardamos la hospitalización
        Hospitalizacion hospitalizacionGuardada = repository.save(hospitalizacion);

        // TAREA 3: Disparamos el cobro automático a Pagos por 200.000
        try {
            Map<String, Object> requestCobro = new HashMap<>();
            requestCobro.put("id_paciente", hospitalizacionGuardada.getId_paciente());
            requestCobro.put("id_referencia_servicio", hospitalizacionGuardada.getId_hospitalizacion());
            requestCobro.put("tipo_servicio", "HOSPITALIZACION");
            requestCobro.put("monto_base", 200000.0);

            pagoClient.procesarCobro(requestCobro);
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudo procesar el cobro de Hospitalización - " + e.getMessage());
        }

        return hospitalizacionGuardada;
    }

    public Hospitalizacion modificarId(Long id, Hospitalizacion hospitalizacionActualizada) {
        Hospitalizacion hospitalizacionExistente = getPorId(id);

        // Validar paciente solo si cambió
        if (!hospitalizacionExistente.getId_paciente().equals(hospitalizacionActualizada.getId_paciente())) {
            try {
                var paciente = pacienteClient.obtenerPacientePorId(hospitalizacionActualizada.getId_paciente());
                if (paciente == null) throw new RuntimeException("Paciente no existe.");
            } catch (Exception e) {
                throw new RuntimeException("Error validando paciente: " + e.getMessage());
            }
        }

        // Validar médico solo si cambió
        if (!hospitalizacionExistente.getId_medico().equals(hospitalizacionActualizada.getId_medico())) {
            try {
                var medico = medicoClient.obtenerMedicoPorId(hospitalizacionActualizada.getId_medico());
                if (medico == null) throw new RuntimeException("Medico no existe.");
            } catch (Exception e) {
                throw new RuntimeException("Error validando medico: " + e.getMessage());
            }
        }


        hospitalizacionExistente.setId_paciente(hospitalizacionActualizada.getId_paciente());
        hospitalizacionExistente.setId_medico(hospitalizacionActualizada.getId_medico());
        hospitalizacionExistente.setCama(hospitalizacionActualizada.getCama());
        hospitalizacionExistente.setMotivo(hospitalizacionActualizada.getMotivo());

        hospitalizacionExistente.setFecha_ingreso(hospitalizacionActualizada.getFecha_ingreso());
        hospitalizacionExistente.setFecha_alta(hospitalizacionActualizada.getFecha_alta());

        return repository.save(hospitalizacionExistente);
    }

    public void eliminarId(Long id) {
        Hospitalizacion hospitalizacion = getPorId(id);
        repository.delete(hospitalizacion);
    }
}