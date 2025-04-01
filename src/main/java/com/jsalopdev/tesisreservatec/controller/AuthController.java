package com.jsalopdev.tesisreservatec.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.jsalopdev.tesisreservatec.entity.Usuario;
import com.jsalopdev.tesisreservatec.repository.UsuarioRepository;
import com.jsalopdev.tesisreservatec.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/usuario")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    private static final String CLIENT_ID = "861236663472-f16n48nq2n53rgjd0g6q69du9c2gp9ru.apps.googleusercontent.com";

    public AuthController(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/validar")
    public ResponseEntity<?> validarUsuario(@RequestParam String idToken) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID)) // Usa el Client ID iOS aquí
                .build();

        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken == null) {
                return ResponseEntity.status(401).body("ID Token inválido");
            }

            String email = googleIdToken.getPayload().getEmail();
            String nombre = (String) googleIdToken.getPayload().get("name");
            String foto = (String) googleIdToken.getPayload().get("picture");

            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(401).body("Usuario no registrado");
            }

            Usuario usuario = usuarioOpt.get();

            // 🚫 Bloquear si el rol no es USER
            if (!"USER".equalsIgnoreCase(usuario.getRol())) {
                return ResponseEntity.status(403).body("Acceso restringido solo para usuarios con rol USER");
            }

            String jwt = jwtUtil.generarToken(usuario);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "✅ Usuario válido");
            response.put("token", jwt);
            response.put("codigo", usuario.getCodigo());
            response.put("nombre", usuario.getNombre());
            response.put("email", usuario.getEmail());
            response.put("rol", usuario.getRol());
            response.put("carrera", usuario.getCarrera());
            response.put("foto", foto);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al verificar token: " + e.getMessage());
        }
    }

}
