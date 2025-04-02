// EspacioService.java
package com.jsalopdev.tesisreservatec.service;

import com.jsalopdev.tesisreservatec.entity.Espacio;

import java.util.List;

public interface EspacioService {
    List<Espacio> listarTodos();
    Espacio guardar(Espacio espacio);
    void eliminar(Long id);
    Espacio buscarPorId(Long id);
}
