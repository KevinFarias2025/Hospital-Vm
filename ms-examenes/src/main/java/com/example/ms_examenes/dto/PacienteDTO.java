package com.example.ms_examenes.dto;

import lombok.Data;

@Data
public class PacienteDTO {
    private Long id_paciente;
    private String nombre;
    private String rut;
}