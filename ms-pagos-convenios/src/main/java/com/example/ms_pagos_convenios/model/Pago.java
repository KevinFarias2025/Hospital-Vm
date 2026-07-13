package com.example.ms_pagos_convenios.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data
@Entity
@Table(name = "pagos")
@SQLDelete(sql = "UPDATE pagos SET estado_activo = false WHERE id_pago=?")
@SQLRestriction("estado_activo = true")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id_pago;

    @NotNull(message = "El id del paciente es obligatorio")
    @Column(nullable = false)
    @Schema(example = "1")
    private Long id_paciente;

    @NotNull(message = "El id de referencia del servicio es obligatorio")
    @Column(nullable = false)
    @Schema(example = "1")
    private Long id_referencia_servicio;

    @NotBlank(message = "El tipo de servicio es obligatorio (Ej: CITA, EXAMEN)")
    @Column(nullable = false)
    @Schema(example = "URGENCIA", allowableValues = {"URGENCIA", "HOSPITALIZACION", "EXAMENES", "CITA"})
    private String tipo_servicio;

    @NotNull(message = "El monto total es obligatorio")
    @PositiveOrZero(message = "El monto no puede ser negativo")
    @Column(nullable = false)
    @Schema(example = "0.0")
    private Double monto_total;

    @NotBlank(message = "El estado del pago es obligatorio")
    @Column(nullable = false)
    @Schema(example = "PENDIENTE", allowableValues = {"PENDIENTE", "PAGADO", "ANULADO"})
    private String estado_pago;

    @Column(nullable = false)
    @Schema(example = "true")
    private boolean estado_activo = true;
}