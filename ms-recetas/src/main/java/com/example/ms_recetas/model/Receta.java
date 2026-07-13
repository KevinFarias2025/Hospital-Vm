package com.example.ms_recetas.model;

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
@Table(name = "recetas")
@SQLDelete(sql = "UPDATE recetas SET estado_activo = false WHERE id_receta=?")
@SQLRestriction("estado_activo = true")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id_receta;

    @NotNull(message = "El id del medico es obligatorio")
    @Column(nullable = false)
    private Long id_medico;

    @NotNull(message = "El id del paciente es obligatorio")
    @Column(nullable = false)
    private Long id_paciente;

    @NotBlank(message = "Los medicamentos son obligatorios")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String medicamentos;

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private Date fecha;

    @Column(nullable = false)
    private boolean estado_activo = true;
}
