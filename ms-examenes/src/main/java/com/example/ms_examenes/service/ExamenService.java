package com.example.ms_examenes.service;

import com.example.ms_examenes.client.MedicoClient;
import com.example.ms_examenes.client.PacienteClient;
import com.example.ms_examenes.client.PagoClient;
import com.example.ms_examenes.model.Examen;
import com.example.ms_examenes.repository.ExamenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExamenService {

    private final ExamenRepository repository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;
    private final PagoClient pagoClient; // Inyección para pagos

    public List<Examen> getGlobal() {
        return repository.findAll();
    }

    public Examen getPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("El examen con ID " + id + " no existe o fue eliminado."));
    }

    public Examen crearId(Examen examen) {
        // Validar Paciente
        try {
            var paciente = pacienteClient.obtenerPacientePorId(examen.getId_paciente());
            if (paciente == null) throw new RuntimeException("Paciente no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error validando paciente: " + e.getMessage());
        }

        // Validar Médico
        try {
            var medico = medicoClient.obtenerMedicoPorId(examen.getId_medico());
            if (medico == null) throw new RuntimeException("Medico no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error validando medico: " + e.getMessage());
        }

        Examen examenGuardado = repository.save(examen);

        // TAREA 3: Disparamos el cobro automático a Pagos por 12.000
        try {
            Map<String, Object> requestCobro = new HashMap<>();
            requestCobro.put("id_paciente", examenGuardado.getId_paciente());
            requestCobro.put("id_referencia_servicio", examenGuardado.getId_examen());
            requestCobro.put("tipo_servicio", "EXAMENES");
            requestCobro.put("monto_base", 12000.0);

            pagoClient.procesarCobro(requestCobro);
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudo procesar el cobro de Examen - " + e.getMessage());
        }

        return examenGuardado;
    }

    // TAREA 1: Método PUT
    public Examen modificarId(Long id, Examen examenActualizado) {
        Examen examenExistente = getPorId(id);

        // Validar paciente solo si cambió
        if (!examenExistente.getId_paciente().equals(examenActualizado.getId_paciente())) {
            try {
                var paciente = pacienteClient.obtenerPacientePorId(examenActualizado.getId_paciente());
                if (paciente == null) throw new RuntimeException("Paciente no existe.");
            } catch (Exception e) {
                throw new RuntimeException("Error validando paciente: " + e.getMessage());
            }
        }

        // Validar médico solo si cambió
        if (!examenExistente.getId_medico().equals(examenActualizado.getId_medico())) {
            try {
                var medico = medicoClient.obtenerMedicoPorId(examenActualizado.getId_medico());
                if (medico == null) throw new RuntimeException("Medico no existe.");
            } catch (Exception e) {
                throw new RuntimeException("Error validando medico: " + e.getMessage());
            }
        }

        // Actualizamos los campos basándonos en tu modelo
        examenExistente.setId_paciente(examenActualizado.getId_paciente());
        examenExistente.setId_medico(examenActualizado.getId_medico());
        examenExistente.setTipo_examen(examenActualizado.getTipo_examen());
        examenExistente.setResultado(examenActualizado.getResultado());

        return repository.save(examenExistente);
    }

    public void eliminarId(Long id) {
        Examen examen = getPorId(id);
        repository.delete(examen);
    }
}