// HorarioServiceImpl.java
package com.jsalopdev.tesisreservatec.service.impl;

import com.jsalopdev.tesisreservatec.entity.Horario;
import com.jsalopdev.tesisreservatec.repository.HorarioRepository;
import com.jsalopdev.tesisreservatec.service.HorarioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorarioServiceImpl implements HorarioService {

    private final HorarioRepository horarioRepository;

    public HorarioServiceImpl(HorarioRepository horarioRepository) {
        this.horarioRepository = horarioRepository;
    }

    @Override
    public List<Horario> listarTodos() {
        return horarioRepository.findAll();
    }

    @Override
    public Horario guardar(Horario horario) {
        return horarioRepository.save(horario);
    }

    @Override
    public void eliminar(Long id) {
        horarioRepository.deleteById(id);
    }

    @Override
    public Horario buscarPorId(Long id) {
        return horarioRepository.findById(id).orElse(null);
    }
}
