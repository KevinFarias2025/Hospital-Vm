package com.example.ms_recetas.service;

import com.example.ms_recetas.client.MedicoClient;
import com.example.ms_recetas.client.PacienteClient;
import com.example.ms_recetas.model.Receta;
import com.example.ms_recetas.repository.RecetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecetaService {

    private final RecetaRepository repository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;

    public List<Receta> getGlobal() {
        return repository.findAll();
    }

    public Receta getPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("La receta con ID " + id + " no existe o fue eliminada logicamente."));
    }

    public Receta crearId(Receta receta) {
        try {
            var paciente = pacienteClient.obtenerPacientePorId(receta.getId_paciente());
            if (paciente == null) throw new RuntimeException("Paciente no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error validando paciente: " + e.getMessage());
        }

        try {
            var medico = medicoClient.obtenerMedicoPorId(receta.getId_medico());
            if (medico == null) throw new RuntimeException("Medico no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error validando medico: " + e.getMessage());
        }

        return repository.save(receta);
    }

    public void eliminarId(Long id) {
        Receta receta = getPorId(id);
        repository.delete(receta);
    }
}