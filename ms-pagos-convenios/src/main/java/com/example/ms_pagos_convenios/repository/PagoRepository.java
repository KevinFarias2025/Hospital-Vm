package com.example.ms_pagos_convenios.repository;

import com.example.ms_pagos_convenios.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
}