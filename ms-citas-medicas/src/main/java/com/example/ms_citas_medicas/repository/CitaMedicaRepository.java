package com.example.ms_citas_medicas.repository;

import com.example.ms_citas_medicas.model.CitaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Long> {
}