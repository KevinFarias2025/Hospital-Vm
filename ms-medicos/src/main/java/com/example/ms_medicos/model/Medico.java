package com.example.ms_medicos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data
@Entity
@Table(name = "medicos")
@SQLDelete(sql = "UPDATE medicos SET estado_activo = false WHERE id_medico=?")
@SQLRestriction("estado_activo = true")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id_medico;

    @NotBlank(message = "El nombre del medico es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotNull(message = "Debe indicar el ID de la especialidad")
    @Column(nullable = false)
    private Long id_especialidad;

    @Column(nullable = false)
    private boolean estado_activo = true;
}