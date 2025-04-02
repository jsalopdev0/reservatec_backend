package com.jsalopdev.tesisreservatec.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuario")
@Data
public class Usuario {

    @Id
    private String codigo;

    private String nombre;

    private String email;

    private String rol;

    private String carrera;

    @Column(name = "foto")
    private String foto;
}
