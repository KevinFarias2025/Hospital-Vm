package com.example.ms_urgencias.repository;

import com.example.ms_urgencias.model.Urgencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrgenciaRepository extends JpaRepository<Urgencia, Long> {
}