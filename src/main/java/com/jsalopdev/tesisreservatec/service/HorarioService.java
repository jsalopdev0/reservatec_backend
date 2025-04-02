// HorarioService.java
package com.jsalopdev.tesisreservatec.service;

import com.jsalopdev.tesisreservatec.entity.Horario;

import java.util.List;

public interface HorarioService {
    List<Horario> listarTodos();
    Horario guardar(Horario horario);
    void eliminar(Long id);
    Horario buscarPorId(Long id);
}
