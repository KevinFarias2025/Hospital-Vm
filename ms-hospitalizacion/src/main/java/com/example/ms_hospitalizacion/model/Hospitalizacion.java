package com.example.ms_hospitalizacion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Date;

@Data
@Entity
@Table(name = "hospitalizaciones")
@SQLDelete(sql = "UPDATE hospitalizaciones SET estado_activo = false WHERE id_hospitalizacion=?")
@SQLRestriction("estado_activo = true")
public class Hospitalizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id_hospitalizacion;

    @NotNull(message = "El ID del paciente es obligatorio")
    @Column(nullable = false)
    private Long id_paciente;

    @NotNull(message = "El ID del medico responsable es obligatorio")
    @Column(nullable = false)
    private Long id_medico;

    @NotBlank(message = "Debe asignar un numero de cama o habitacion")
    @Column(nullable = false)
    private String cama;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    @Column(nullable = false)
    private Date fecha_ingreso;

    private Date fecha_alta;

    @NotBlank(message = "El motivo de hospitalizacion es obligatorio")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String motivo;

    @Column(nullable = false)
    private boolean estado_activo = true;
}