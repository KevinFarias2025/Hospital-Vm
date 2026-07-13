package com.example.ms_pagos_convenios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class PagoRequestDTO {

    @NotNull(message = "El id del paciente es obligatorio")
    @Schema(example = "1", description = "ID del paciente")
    private Long id_paciente;

    @NotNull(message = "El id de referencia es obligatorio")
    @Schema(example = "1", description = "El ID de la cita, urgencia, examen, etc.")
    private Long id_referencia_servicio;

    @NotBlank(message = "El tipo de servicio es obligatorio")
    @Schema(description = "Categoría del servicio cobrado", example = "URGENCIA", allowableValues = {"URGENCIA", "HOSPITALIZACION", "EXAMENES", "CITA"})
    private String tipo_servicio;

    @NotNull(message = "El monto base es obligatorio")
    @PositiveOrZero(message = "El monto no puede ser negativo")
    @Schema(example = "0.0", description = "Monto original sin descuento")
    private Double monto_base;
}