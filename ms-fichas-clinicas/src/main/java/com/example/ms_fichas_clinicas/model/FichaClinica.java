package com.example.ms_fichas_clinicas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data
@Entity
@Table(name = "fichas_clinicas")
@SQLDelete(sql = "UPDATE fichas_clinicas SET estado_activo = false WHERE id_ficha=?")
@SQLRestriction("estado_activo = true")
public class FichaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id_ficha;

    @NotNull(message = "El ID del paciente es obligatorio")
    @Column(nullable = false)
    private Long id_paciente;

    @NotBlank(message = "Los antecedentes son obligatorios")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String antecedentes;

    private String alergias;
    private String grupo_sanguineo;

    @Column(nullable = false)
    private boolean estado_activo = true;
}