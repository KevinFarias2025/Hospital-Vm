package com.example.ms_pacientes.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data
@Entity
@Table(name = "pacientes")
@SQLDelete(sql = "UPDATE pacientes SET estado_activo = false WHERE id_paciente=?")
@SQLRestriction("estado_activo = true")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id_paciente;

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^[0-9]+-[0-9kK]{1}$", message = "El formato del RUT no es valido (ej: 12345678-9)")
    private String rut;

    @NotBlank(message = "El nombre del paciente no puede estar vacio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad no puede ser un numero negativo")
    @Max(value = 130, message = "Edad fuera de rango permitido")
    private Integer edad;

    @Column(nullable = false)
    private boolean estado_activo = true;
}