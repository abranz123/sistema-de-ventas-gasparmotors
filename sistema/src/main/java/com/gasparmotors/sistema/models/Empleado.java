package com.gasparmotors.sistema.models;

import jakarta.persistence.*;

@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_empleado;

    private String nombre;
    private String apellido;
    private String telefono;
    private String rol;

    // Métodos Getters y Setters
    public Long getId_empleado() { return id_empleado; }
    public void setId_empleado(Long id_empleado) { this.id_empleado = id_empleado; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}