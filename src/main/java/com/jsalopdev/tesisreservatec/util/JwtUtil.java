package com.jsalopdev.tesisreservatec.util;

import com.jsalopdev.tesisreservatec.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.ms}")
    private long expirationMs;

    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generarToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("nombre", usuario.getNombre());
        claims.put("email", usuario.getEmail());
        claims.put("rol", usuario.getRol());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getCodigo())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    // ✅ Valida si el token es válido y tiene el rol ADMIN (para uso web)
    public boolean validarTokenYEsAdmin(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String rol = (String) claims.get("rol");
            return "ADMIN".equalsIgnoreCase(rol);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
