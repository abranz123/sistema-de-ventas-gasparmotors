package com.gasparmotors.sistema.models;

import jakarta.persistence.*;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_categoria;

    @Column(name = "nombre_categoria", nullable = false, unique = true)
    private String nombre_categoria;

    // Constructor vacío obligatorio para Spring Boot
    public Categoria() {
    }

    // Getters y Setters
    public Integer getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(Integer id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNombre_categoria() {
        return nombre_categoria;
    }

    public void setNombre_categoria(String nombre_categoria) {
        this.nombre_categoria = nombre_categoria;
    }
}