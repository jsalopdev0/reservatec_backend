package com.jsalopdev.tesisreservatec.controller;

import com.jsalopdev.tesisreservatec.entity.Reserva;
import com.jsalopdev.tesisreservatec.service.ReservaService;
import com.jsalopdev.tesisreservatec.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final JwtUtil jwtUtil;

    public ReservaController(ReservaService reservaService, JwtUtil jwtUtil) {
        this.reservaService = reservaService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<?> listarReservas(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Claims claims = jwtUtil.obtenerClaims(token);
        String rol = (String) claims.get("rol");

        if (!"ADMIN".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(403).body("Acceso denegado");
        }

        return ResponseEntity.ok(reservaService.listarTodas());
    }

    @PostMapping
    public Reserva guardar(@RequestBody Reserva reserva) {
        return reservaService.guardar(reserva);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Claims claims = jwtUtil.obtenerClaims(token);
        String rol = (String) claims.get("rol");
        String codigoUsuario = claims.getSubject(); // El código del usuario está en el subject

        Reserva reserva = reservaService.buscarPorId(id);

        if ("ADMIN".equalsIgnoreCase(rol) || reserva.getUsuario().getCodigo().equals(codigoUsuario)) {
            return ResponseEntity.ok(reserva);
        } else {
            return ResponseEntity.status(403).body("Acceso denegado");
        }
    }

    @GetMapping("/misreservas")
    public ResponseEntity<?> listarMisReservas(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Claims claims = jwtUtil.obtenerClaims(token);
        String codigo = claims.getSubject(); // El subject es el código del usuario

        List<Reserva> reservas = reservaService.listarPorUsuarioCodigo(codigo);
        return ResponseEntity.ok(reservas);
    }


}
