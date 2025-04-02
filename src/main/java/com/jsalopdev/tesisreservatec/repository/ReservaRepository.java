package com.jsalopdev.tesisreservatec.repository;

import com.jsalopdev.tesisreservatec.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUsuarioCodigo(String codigo); // útil para mostrar reservas por usuario

}
