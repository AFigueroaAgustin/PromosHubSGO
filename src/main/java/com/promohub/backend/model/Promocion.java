package com.promohub.backend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "promociones", indexes = {
        @Index(name = "idx_promo_banco_cat", columnList = "banco_id, categoria"),
        @Index(name = "idx_promo_fecha_fin", columnList = "fecha_fin"),
        @Index(name = "idx_promo_upsert", columnList = "banco_id, categoria, titulo")
})
@Getter
@Setter
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // hace que sea obligatorio este campo
    private String titulo;
    @Column(columnDefinition = "TEXT") // hace que permita mas de 255 caracteres
    private String descripcion;
    @Column(name = "porcentaje_descuento")
    private Double porcentajeDescuento;
    @Column(name = "tope_reintegro")
    private Double topeReintegro;
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
    @Column(name = "dias_aplicacion", length = 150)
    private String diasAplicacion;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Categoria categoria=Categoria.VARIOS;

    //RELACIONES
    @ManyToOne(fetch = FetchType.LAZY) // esto es para que cargue de manera los datos cuando lo necesite.
    @JoinColumn(name = "banco_id", nullable = false)
    private Banco banco;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "promocion_comercios",
            joinColumns = @JoinColumn(name = "promocion_id"),// La columna que hace referencia a la clave primaria
            inverseJoinColumns = @JoinColumn(name = "comercio_id")) //La columna que hace referencia a la clave primaria de la otra entidad
    private List<Comercio> comercios;




    public Promocion() {
    }

    public Promocion(Long id, String titulo, String descripcion,
                     Double porcentajeDeDescuento, Double topeReintegro,
                     LocalDate fechaInicio, LocalDate fechaFin, String diasAplicacion,
                     Categoria categoria, Banco banco, List<Comercio> comercios) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.porcentajeDescuento = porcentajeDeDescuento;
        this.topeReintegro = topeReintegro;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.diasAplicacion = diasAplicacion;
        this.categoria = categoria;
        this.banco = banco;
        this.comercios = comercios;
    }
    public boolean isVigente() {
        LocalDate hoy = LocalDate.now();
        return (fechaInicio != null && fechaFin != null)
                && (!hoy.isBefore(fechaInicio) && !hoy.isAfter(fechaFin));
    }
}