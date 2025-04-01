package com.jsalopdev.tesisreservatec.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Usuario {

    @Id
    private String codigo;

    private String nombre;
    private String email;
    private String carrera;
    private String rol;
}
