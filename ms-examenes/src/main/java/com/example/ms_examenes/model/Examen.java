package com.example.ms_examenes.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data
@Entity
@Table(name = "examenes")
@SQLDelete(sql = "UPDATE examenes SET estado_activo = false WHERE id_examen=?")
@SQLRestriction("estado_activo = true")
public class Examen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id_examen;

    @NotNull(message = "Debe indicar el paciente")
    @Column(nullable = false)
    private Long id_paciente;

    @NotNull(message = "Debe indicar el medico")
    @Column(nullable = false)
    private Long id_medico;

    @NotBlank(message = "El tipo de examen es obligatorio")
    @Column(nullable = false)
    private String tipo_examen;

    @NotBlank(message = "El resultado es obligatorio")
    @Column(nullable = false)
    private String resultado;

    @Column(nullable = false)
    private boolean estado_activo = true;
}