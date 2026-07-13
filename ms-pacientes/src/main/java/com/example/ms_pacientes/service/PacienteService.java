package com.example.ms_pacientes.service;

import com.example.ms_pacientes.model.Paciente;
import com.example.ms_pacientes.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository repository;

    public List<Paciente> getGlobal() {
        return repository.findAll();
    }

    public Paciente getPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("El paciente con ID " + id + " no existe o fue eliminado logicamente."));
    }

    public Paciente crearId(Paciente paciente) {
        // Aseguramos que la letra entre siempre en mayúscula a la BD
        paciente.setPrevision(paciente.getPrevision().toUpperCase());
        return repository.save(paciente);
    }

    public Paciente modificarId(Long id, Paciente pacienteActualizado) {
        Paciente pacienteExistente = getPorId(id);

        pacienteExistente.setRut(pacienteActualizado.getRut());
        pacienteExistente.setNombre(pacienteActualizado.getNombre());
        pacienteExistente.setEdad(pacienteActualizado.getEdad());
        pacienteExistente.setPrevision(pacienteActualizado.getPrevision().toUpperCase());
        pacienteExistente.setEstado_activo(pacienteActualizado.isEstado_activo());

        return repository.save(pacienteExistente);
    }

    public void eliminarId(Long id) {
        Paciente paciente = getPorId(id);
        repository.delete(paciente);
    }
}