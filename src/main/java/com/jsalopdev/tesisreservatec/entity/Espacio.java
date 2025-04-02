package com.jsalopdev.tesisreservatec.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Espacio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private int aforo;

    @Enumerated(EnumType.STRING)
    private EstadoEspacio estado;
}
