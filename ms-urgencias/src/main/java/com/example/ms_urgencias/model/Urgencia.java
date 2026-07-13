package com.example.ms_urgencias.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data
@Entity
@Table(name = "urgencias")
@SQLDelete(sql = "UPDATE urgencias SET estado_activo = false WHERE id_urgencia=?")
@SQLRestriction("estado_activo = true")
public class Urgencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id_urgencia;

    @NotNull(message = "El id del paciente es obligatorio")
    @Column(nullable = false)
    private Long id_paciente;

    @NotNull(message = "El id del medico es obligatorio")
    @Column(nullable = false)
    private Long id_medico;

    @NotBlank(message = "El nivel de gravedad es obligatorio")
    @Column(nullable = false)
    private String nivel_gravedad;

    @NotBlank(message = "El motivo de la urgencia es obligatorio")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String motivo;

    @Column(nullable = false)
    private boolean estado_activo = true;
}