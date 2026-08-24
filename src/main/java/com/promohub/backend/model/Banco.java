package com.promohub.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bancos")
@Setter
@Getter
public class Banco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 100) //Permite que sea obligatorio el campo,no permite que haya repetidos en esta calumnas,permite hasta 100 caract.
    private String nombre;

    //Identificador simple para el bot de Python (ej: "BSE", "MACRO")
    @Column(name = "codigo_identificador",length = 50)
    private String codigoIdentificador;

    // Sitio web oficial del banco
    @Column(name = "sitio_web")
    private String sitioWeb;

    // Logo para las tarjetas visuales de Bootstrap
    @Column(name = "url_logo")
    private String urlLogo;

    // Tipo de entidad: Tarjeta Regional, Banco Tradicional o Billetera Virtual
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_emisor", length = 50)
    private TipoEmisor tipoEmisor = TipoEmisor.BANCO_TRADICIONAL;

    // Constructores
    public Banco() {
    }

    public Banco(Long id, String nombre, String codigoIdentificador, String sitioWeb, String urlLogo, TipoEmisor tipoEmisor) {
        this.id = id;
        this.nombre = nombre;
        this.codigoIdentificador = codigoIdentificador;
        this.sitioWeb = sitioWeb;
        this.urlLogo = urlLogo;
        this.tipoEmisor = tipoEmisor;
    }
}
