// ReservaService.java
package com.jsalopdev.tesisreservatec.service;

import com.jsalopdev.tesisreservatec.entity.Reserva;

import java.util.List;

public interface ReservaService {
    List<Reserva> listarTodas();
    Reserva guardar(Reserva reserva);
    void eliminar(Long id);
    Reserva buscarPorId(Long id);
    List<Reserva> listarPorUsuarioCodigo(String codigo);

}
