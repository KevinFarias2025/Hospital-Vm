package com.example.ms_pagos_convenios.service;

import com.example.ms_pagos_convenios.client.PacienteClient;
import com.example.ms_pagos_convenios.dto.PagoRequestDTO;
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
                .orElseThrow(() -> new RuntimeException("El pago con ID " + id + " no existe."));
    }

    public Pago procesarCobro(PagoRequestDTO request) {
        // 1. Validar Paciente y obtener previsión
        var paciente = pacienteClient.obtenerPacientePorId(request.getId_paciente());
        if (paciente == null) throw new RuntimeException("Paciente no encontrado");

        String prev = paciente.getPrevision() != null ? paciente.getPrevision().toUpperCase() : "C";
        double montoFinal = request.getMonto_base();

        // 2. Aplicar descuentos
        switch (prev) {
            case "A": montoFinal *= 0.70; break;
            case "B": montoFinal -= 4000; break;
            default: break; // C u otros pagan completo
        }

        montoFinal = Math.max(0, montoFinal); // Evitar negativos

        // 3. Guardar con tu modelo exacto
        Pago pago = new Pago();
        pago.setId_paciente(request.getId_paciente());
        pago.setId_referencia_servicio(request.getId_referencia_servicio());
        pago.setTipo_servicio(request.getTipo_servicio());
        pago.setMonto_total(montoFinal);
        pago.setEstado_pago("PENDIENTE"); // Estado por defecto

        return repository.save(pago);
    }

    public void eliminarId(Long id) {
        Pago p = getPorId(id);
        repository.delete(p);
    }
}