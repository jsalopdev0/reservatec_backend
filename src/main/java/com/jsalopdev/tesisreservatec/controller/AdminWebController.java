package com.jsalopdev.tesisreservatec.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.jsalopdev.tesisreservatec.entity.Usuario;
import com.jsalopdev.tesisreservatec.entity.GoogleOAuthProperties;
import com.jsalopdev.tesisreservatec.repository.UsuarioRepository;
import com.jsalopdev.tesisreservatec.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminWebController {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final GoogleOAuthProperties googleOAuthProperties;

    public AdminWebController(UsuarioRepository usuarioRepository, JwtUtil jwtUtil, GoogleOAuthProperties googleOAuthProperties) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.googleOAuthProperties = googleOAuthProperties;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "admin_login";
    }

    @PostMapping("/validar")
    public void validarAdmin(@RequestParam String idToken, HttpServletResponse response) throws IOException, GeneralSecurityException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(googleOAuthProperties.getClientIds()) // ✅ Usa todos los client IDs válidos
                .build();

        GoogleIdToken googleIdToken = verifier.verify(idToken);
        if (googleIdToken == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "ID Token inválido");
            return;
        }

        String email = googleIdToken.getPayload().getEmail();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty() || !"ADMIN".equalsIgnoreCase(usuarioOpt.get().getRol())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso denegado");
            return;
        }

        Usuario usuario = usuarioOpt.get();
        String jwt = jwtUtil.generarToken(usuario);

        Cookie cookie = new Cookie("admin_token", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/admin");
        cookie.setMaxAge(3600);
        // Opcional para seguridad extra:
        // cookie.setSecure(true); // usar solo si tienes HTTPS
        // cookie.setDomain("tudominio.com"); // si aplica

        response.addCookie(cookie);
        response.sendRedirect("/admin/inicio");
    }
    // cookie.setSecure(true); // usar solo si tienes HTTPS
    // cookie.setDomain("tudominio.com"); // si aplica


    @GetMapping("/inicio")
    public String inicio(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        String jwt = null;

        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("admin_token".equals(c.getName())) {
                    jwt = c.getValue();
                    break;
                }
            }
        }

        if (jwt == null || !jwtUtil.validarTokenYEsAdmin(jwt)) {
            response.sendRedirect("/admin/login");
            return null;
        }

        return "admin_home";
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) throws IOException {
        Cookie cookie = new Cookie("admin_token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/admin");
        response.addCookie(cookie);
        response.sendRedirect("/admin/login");
    }
}
