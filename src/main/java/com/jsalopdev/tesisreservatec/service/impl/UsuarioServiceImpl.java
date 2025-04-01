package com.jsalopdev.tesisreservatec.service.impl;

import com.jsalopdev.tesisreservatec.client.UsuarioClient;
import com.jsalopdev.tesisreservatec.entity.Usuario;
import com.jsalopdev.tesisreservatec.repository.UsuarioRepository;
import com.jsalopdev.tesisreservatec.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioClient usuarioClient;

    @Override
    public void sincronizar() {
        List<Usuario> externos = usuarioClient.obtenerUsuarios();
        List<Usuario> locales = usuarioRepository.findAll();

        Map<String, Usuario> mapaLocal = new HashMap<>();
        locales.forEach(u -> mapaLocal.put(u.getCodigo(), u));

        Set<String> codigosExternos = new HashSet<>();

        for (Usuario externo : externos) {
            codigosExternos.add(externo.getCodigo());
            Usuario local = mapaLocal.get(externo.getCodigo());

            if (local == null) {
                usuarioRepository.save(externo); // nuevo
            } else if (!externo.equals(local)) {
                usuarioRepository.save(externo); // actualizado
            }
        }

        for (Usuario local : locales) {
            if (!codigosExternos.contains(local.getCodigo())) {
                usuarioRepository.deleteById(local.getCodigo()); // eliminado
            }
        }
    }
}
