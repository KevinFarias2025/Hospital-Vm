package com.example.ms_pagos_convenios.service;

import com.example.ms_pagos_convenios.client.PacienteClient;
import com.example.ms_pagos_convenios.model.Pago;
import com.example.ms_pagos_convenios.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository repository;
    private final PacienteClient pacienteClient;

    public List<Pago> getGlobal() {
        return repository.findAll();
    }

    public Pago getPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("El pago con ID " + id + " no existe o fue eliminado logicamente."));
    }

    public Pago crearId(Pago pago) {
        try {
            var paciente = pacienteClient.obtenerPacientePorId(pago.getId_paciente());
            if (paciente == null) throw new RuntimeException("Paciente no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error validando paciente: " + e.getMessage());
        }

        return repository.save(pago);
    }

    public void eliminarId(Long id) {
        Pago pago = getPorId(id);
        repository.delete(pago);
    }
}