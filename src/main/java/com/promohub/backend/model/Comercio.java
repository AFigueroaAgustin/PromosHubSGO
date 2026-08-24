package com.promohub.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comercios", indexes = {
        @Index(name = "idx_comercio_nombre", columnList = "nombre"),
        @Index(name = "idx_comercio_ciudad", columnList = "ciudad")
})
@Getter
@Setter
public class Comercio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 150)
    private String nombre;

    // Rubro o categoría del comercio (ej: "SUPERMERCADO", "GASTRONOMIA", "COMBUSTIBLE", "FARMACIA")
    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private Categoria categoria=Categoria.VARIOS;

    // Dirección física (ej: "Av. Belgrano Sur 1450")
    @Column(length = 200)
    private String direccion;

    // Ciudad para filtrar fácilmente (ej: "Santiago del Estero", "La Banda", "Termas de Río Hondo")
    @Column(length = 100)
    private String ciudad;

    // Campos preparados para la futura geolocalización por GPS
    private Double latitud;
    private Double longitud;

    //Constructores
    public Comercio() {
    }

    public Comercio(Long id, String nombre, Categoria categoria, String direccion, String ciudad, Double latitud, Double longitud) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.latitud = latitud;
        this.longitud = longitud;
    }
}
