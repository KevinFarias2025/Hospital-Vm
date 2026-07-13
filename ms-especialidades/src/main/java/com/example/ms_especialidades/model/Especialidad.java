package com.example.ms_especialidades.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data
@Entity
@Table(name = "especialidades")
// Sobreescribe el comportamiento de "deleteById" del repositorio
@SQLDelete(sql = "UPDATE especialidades SET estado_activo = false WHERE id_especialidad=?")
// Filtra automáticamente los inactivos en todos los "findAll" o "findById"
/* Basicamente con esos dos @ hacemos que el registro quede limpio cuando mandamos un delete, por si acaso por asi decirlo */
@SQLRestriction("estado_activo = true")
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Luego lo habilitamos y ponemos en un modo que nos sirva kevs
    private Long id_especialidad;

    @Column(nullable = false)
    @NotBlank(message = "Ingrese el nombre de la especialidad.")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
    private String nombre;

    @Column(nullable = false)   // Esta cosa es para habilitar el tema de que la tabla aplique el true o false para que oculte los false cuando se haga un get
    private boolean estado_activo = true;
}