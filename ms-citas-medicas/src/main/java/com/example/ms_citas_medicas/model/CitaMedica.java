package com.example.ms_citas_medicas.model;

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
@Table(name = "citas_medicas")
// Sobreescribe el comportamiento de "deleteById" del repositorio
@SQLDelete(sql = "UPDATE citas_medicas SET estado_activo = false WHERE id_cita=?")
// Filtra automáticamente los inactivos en todos los "findAll" o "findById"
/* Basicamente con esos dos @ hacemos que el registro quede limpio cuando mandamos un delete, por si acaso por asi decirlo */
@SQLRestriction("estado_activo = true")
public class CitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Esta wea hace que no te de el espacio para rellenar en el json el id de la entidad, ya que Spring data jpa da el id automaticamente
    private Long id_cita;

    @Column(nullable = false)
    @NotNull(message = "El id del paciente no puede estar vacio.")
    private Long id_paciente;

    @Column(nullable = false)
    @NotNull(message = "El id del medico no puede estar vacio.")
    private Long id_medico;

    @Column(nullable = false)
    @NotNull(message = "Debes tomar una hora")
    private Date fecha_hora;

    @Column(nullable = false)
    @NotBlank(message = "Coloca un estado entre estos PENDIENTE, CONFIRMADA o CANCELADA.")
    private String estado_cita;

    @Column(nullable = false)   // Esta cosa es para habilitar el tema de que la tabla aplique el true o false para que oculte los false cuando se haga un get
    private boolean estado_activo = true;
}