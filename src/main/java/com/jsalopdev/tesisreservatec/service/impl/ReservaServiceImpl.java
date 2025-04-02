// ReservaServiceImpl.java
package com.jsalopdev.tesisreservatec.service.impl;

import com.jsalopdev.tesisreservatec.entity.Reserva;
import com.jsalopdev.tesisreservatec.repository.ReservaRepository;
import com.jsalopdev.tesisreservatec.service.ReservaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaServiceImpl(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Override
    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    @Override
    public Reserva guardar(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    @Override
    public void eliminar(Long id) {
        reservaRepository.deleteById(id);
    }

    @Override
    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Reserva> listarPorUsuarioCodigo(String codigo) {
        return reservaRepository.findByUsuarioCodigo(codigo);
    }

}
