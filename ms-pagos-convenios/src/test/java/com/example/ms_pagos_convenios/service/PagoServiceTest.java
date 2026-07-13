package com.example.ms_pagos_convenios.service;

import com.example.ms_pagos_convenios.client.PacienteClient;
import com.example.ms_pagos_convenios.dto.PacienteDTO;
import com.example.ms_pagos_convenios.dto.PagoRequestDTO;
import com.example.ms_pagos_convenios.model.Pago;
import com.example.ms_pagos_convenios.repository.PagoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest {

    @Mock
    private PagoRepository repository;

    @Mock // Fingimos el FeignClient que conecta con ms-pacientes
    private PacienteClient pacienteClient;

    @InjectMocks
    private PagoService service;

    @Test
    void procesarCobro_PrevisionA_DebeAplicarDescuento30Porciento() {
        // 1. Preparar datos
        PagoRequestDTO request = new PagoRequestDTO();
        request.setId_paciente(1L);
        request.setMonto_base(10000.0);

        PacienteDTO mockPaciente = new PacienteDTO();
        mockPaciente.setPrevision("A"); // Beneficio Isapre

        // 2. Programar a Mockito
        Mockito.when(pacienteClient.obtenerPacientePorId(1L)).thenReturn(mockPaciente);
        Mockito.when(repository.save(any(Pago.class))).thenAnswer(i -> i.getArguments()[0]);

        // 3. Ejecutar
        Pago resultado = service.procesarCobro(request);

        // 4. Comprobar (10000 * 0.70 = 7000)
        assertEquals(7000.0, resultado.getMonto_total());
        assertEquals("PENDIENTE", resultado.getEstado_pago());
    }

    @Test
    void procesarCobro_PrevisionB_DebeDescontar4000() {
        PagoRequestDTO request = new PagoRequestDTO();
        request.setId_paciente(2L);
        request.setMonto_base(10000.0);

        PacienteDTO mockPaciente = new PacienteDTO();
        mockPaciente.setPrevision("B"); // Beneficio Fonasa

        Mockito.when(pacienteClient.obtenerPacientePorId(2L)).thenReturn(mockPaciente);
        Mockito.when(repository.save(any(Pago.class))).thenAnswer(i -> i.getArguments()[0]);

        Pago resultado = service.procesarCobro(request);

        // 10000 - 4000 = 6000
        assertEquals(6000.0, resultado.getMonto_total());
    }

    @Test
    void procesarCobro_PrevisionC_DebeCobrarCompleto() {
        PagoRequestDTO request = new PagoRequestDTO();
        request.setId_paciente(3L);
        request.setMonto_base(10000.0);

        PacienteDTO mockPaciente = new PacienteDTO();
        mockPaciente.setPrevision("C"); // Sin beneficio

        Mockito.when(pacienteClient.obtenerPacientePorId(3L)).thenReturn(mockPaciente);
        Mockito.when(repository.save(any(Pago.class))).thenAnswer(i -> i.getArguments()[0]);

        Pago resultado = service.procesarCobro(request);

        // Se mantiene en 10000
        assertEquals(10000.0, resultado.getMonto_total());
    }

    @Test
    void procesarCobro_PrevisionB_MontoMenorA4000_DebeSerCero() {
        PagoRequestDTO request = new PagoRequestDTO();
        request.setId_paciente(4L);
        request.setMonto_base(3000.0); // Monto base menor al descuento fijo

        PacienteDTO mockPaciente = new PacienteDTO();
        mockPaciente.setPrevision("B");

        Mockito.when(pacienteClient.obtenerPacientePorId(4L)).thenReturn(mockPaciente);
        Mockito.when(repository.save(any(Pago.class))).thenAnswer(i -> i.getArguments()[0]);

        Pago resultado = service.procesarCobro(request);

        // 3000 - 4000 = -1000 -> Math.max(0, -1000) = 0.0
        assertEquals(0.0, resultado.getMonto_total());
    }

    @Test
    void procesarCobro_PacienteNoExiste_DebeLanzarExcepcion() {
        PagoRequestDTO request = new PagoRequestDTO();
        request.setId_paciente(99L);
        request.setMonto_base(10000.0);

        // Mockito devuelve null simulando que el FeignClient no encontró al paciente
        Mockito.when(pacienteClient.obtenerPacientePorId(99L)).thenReturn(null);

        // Comprobamos que el servicio tira la excepción correcta y corta la ejecución
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.procesarCobro(request);
        });

        assertEquals("Paciente no encontrado", exception.getMessage());
    }
}