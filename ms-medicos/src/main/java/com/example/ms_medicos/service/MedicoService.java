package com.example.ms_medicos.service;

import com.example.ms_medicos.client.EspecialidadClient;
import com.example.ms_medicos.model.Medico;
import com.example.ms_medicos.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository repository;
    private final EspecialidadClient especialidadClient;

    public List<Medico> getGlobal() {
        return repository.findAll();
    }

    public Medico getPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("El medico con ID " + id + " no existe o fue eliminado."));
    }

    public Medico crearId(Medico medico) {
        try {
            var especialidad = especialidadClient.obtenerEspecialidadPorId(medico.getId_especialidad());
            if (especialidad == null) {
                throw new RuntimeException("La especialidad con ID " + medico.getId_especialidad() + " no existe.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al validar la especialidad: " + e.getMessage());
        }

        return repository.save(medico);
    }

    public Medico modificarId(Long id, Medico medicoActualizado) {
        Medico medicoExistente = getPorId(id);

        // Validar la especialidad solo si el ID enviado en el JSON es distinto al que ya tenía
        if (!medicoExistente.getId_especialidad().equals(medicoActualizado.getId_especialidad())) {
            try {
                var especialidad = especialidadClient.obtenerEspecialidadPorId(medicoActualizado.getId_especialidad());
                if (especialidad == null) {
                    throw new RuntimeException("La especialidad con ID " + medicoActualizado.getId_especialidad() + " no existe.");
                }
            } catch (Exception e) {
                throw new RuntimeException("Error al validar la especialidad: " + e.getMessage());
            }
        }

        medicoExistente.setNombre(medicoActualizado.getNombre());
        medicoExistente.setId_especialidad(medicoActualizado.getId_especialidad());
        medicoExistente.setEstado_activo(medicoActualizado.isEstado_activo());

        return repository.save(medicoExistente);
    }

    public void eliminarId(Long id) {
        Medico medico = getPorId(id);
        repository.delete(medico);
    }
}