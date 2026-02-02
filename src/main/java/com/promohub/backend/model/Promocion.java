package com.promohub.backend.model;


import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entidad;
    private String titulo;
    private String categoria;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @ElementCollection
    private List<String> comercios;

    //Constructores

    public Promocion() {
    }

    public Promocion(Long id, String entidad, String titulo, String categoria, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, List<String> comercios) {
        this.id = id;
        this.entidad = entidad;
        this.titulo = titulo;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.comercios = comercios;
    }

    
    
    // Getters
    public Long getId() {
        return id;
    }

    public String getEntidad() {
        return entidad;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public List<String> getComercios() {
        return comercios;
    }

    
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setComercios(List<String> comercios) {
        this.comercios = comercios;
    }

    public boolean estaVigente(LocalDate fecha) {
    return (fecha.isEqual(fechaInicio) ||fecha.isAfter(fechaInicio))
        && (fecha.isEqual(fechaFin) || fecha.isBefore(fechaFin));
}


}
