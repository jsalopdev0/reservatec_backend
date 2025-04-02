package com.jsalopdev.tesisreservatec.controller;

import com.jsalopdev.tesisreservatec.entity.Usuario;
import com.jsalopdev.tesisreservatec.repository.UsuarioRepository;
import com.jsalopdev.tesisreservatec.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public UsuarioController(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    // ✅ Solo ADMIN puede ver todos
    @GetMapping
    public ResponseEntity<?> listarTodos(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Claims claims = jwtUtil.obtenerClaims(token);
        String rol = (String) claims.get("rol");

        if (!"ADMIN".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(403).body("Acceso denegado");
        }

        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    // ✅ El usuario autenticado puede ver solo sus datos
    @GetMapping("/me")
    public ResponseEntity<?> verPerfil(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Claims claims = jwtUtil.obtenerClaims(token);
        String codigo = claims.getSubject();

        return usuarioRepository.findById(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
