package com.example.ms_recetas.service;

import com.example.ms_recetas.client.MedicoClient;
import com.example.ms_recetas.client.PacienteClient;
import com.example.ms_recetas.client.PagoClient;
import com.example.ms_recetas.model.Receta;
import com.example.ms_recetas.repository.RecetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecetaService {

    private final RecetaRepository repository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;
    private final PagoClient pagoClient; // Inyección de pagos

    public List<Receta> getGlobal() {
        return repository.findAll();
    }

    public Receta getPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("La receta con ID " + id + " no existe o fue eliminada lógicamente."));
    }

    public Receta crearId(Receta receta) {
        // Validar Paciente
        try {
            var paciente = pacienteClient.obtenerPacientePorId(receta.getId_paciente());
            if (paciente == null) throw new RuntimeException("Paciente no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error al validar paciente: " + e.getMessage());
        }

        // Validar Médico
        try {
            var medico = medicoClient.obtenerMedicoPorId(receta.getId_medico());
            if (medico == null) throw new RuntimeException("Médico no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error al validar médico: " + e.getMessage());
        }

        // Guardamos la receta
        Receta recetaGuardada = repository.save(receta);

        // TAREA 3: Cobro automático a pagos por $15.000
        try {
            Map<String, Object> requestCobro = new HashMap<>();
            requestCobro.put("id_paciente", recetaGuardada.getId_paciente());
            requestCobro.put("id_referencia_servicio", recetaGuardada.getId_receta());
            requestCobro.put("tipo_servicio", "RECETA");
            requestCobro.put("monto_base", 15000.0);

            pagoClient.procesarCobro(requestCobro);
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudo procesar el cobro de la Receta - " + e.getMessage());
        }

        return recetaGuardada;
    }

    // TAREA 1: Método PUT para Modificar
    public Receta modificarId(Long id, Receta recetaActualizada) {
        Receta recetaExistente = getPorId(id);

        // Validar paciente solo si cambió
        if (!recetaExistente.getId_paciente().equals(recetaActualizada.getId_paciente())) {
            try {
                var paciente = pacienteClient.obtenerPacientePorId(recetaActualizada.getId_paciente());
                if (paciente == null) throw new RuntimeException("Paciente no existe.");
            } catch (Exception e) {
                throw new RuntimeException("Error validando paciente: " + e.getMessage());
            }
        }

        // Validar médico solo si cambió
        if (!recetaExistente.getId_medico().equals(recetaActualizada.getId_medico())) {
            try {
                var medico = medicoClient.obtenerMedicoPorId(recetaActualizada.getId_medico());
                if (medico == null) throw new RuntimeException("Médico no existe.");
            } catch (Exception e) {
                throw new RuntimeException("Error validando médico: " + e.getMessage());
            }
        }

        // Actualizamos los campos basados en tu modelo
        recetaExistente.setId_paciente(recetaActualizada.getId_paciente());
        recetaExistente.setId_medico(recetaActualizada.getId_medico());
        recetaExistente.setMedicamentos(recetaActualizada.getMedicamentos());
        recetaExistente.setFecha(recetaActualizada.getFecha());

        return repository.save(recetaExistente);
    }

    public void eliminarId(Long id) {
        Receta receta = getPorId(id);
        repository.delete(receta);
    }
}