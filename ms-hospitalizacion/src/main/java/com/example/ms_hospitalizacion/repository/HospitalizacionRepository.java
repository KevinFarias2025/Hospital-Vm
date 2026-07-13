package com.example.ms_hospitalizacion.repository;

import com.example.ms_hospitalizacion.model.Hospitalizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalizacionRepository extends JpaRepository<Hospitalizacion, Long> {
}