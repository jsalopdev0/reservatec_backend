package com.jsalopdev.tesisreservatec.repository;

import com.jsalopdev.tesisreservatec.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);

}
