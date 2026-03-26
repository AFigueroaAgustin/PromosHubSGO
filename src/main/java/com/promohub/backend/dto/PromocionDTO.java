package com.promohub.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public class PromocionDTO {
    private Long id;
    @NotBlank
    private String entidad;
    @NotBlank
    private String categoria;
    @NotBlank
    private String titulo;
    private String descripcion;

    @JsonProperty("comercios_adheridos")
    private List<String> comerciosAdheridos;

    @JsonProperty("legales_observaciones")
    private String legalesObservaciones; // ¡Ahora sí, siempre String!
    @NotNull
    @Valid
    private VigenciaDTO vigencia;

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getComerciosAdheridos() {
        return comerciosAdheridos;
    }

    public void setComerciosAdheridos(List<String> comerciosAdheridos) {
        this.comerciosAdheridos = comerciosAdheridos;
    }

    public String getLegalesObservaciones() {
        return legalesObservaciones;
    }

    public void setLegalesObservaciones(String legalesObservaciones) {
        this.legalesObservaciones = legalesObservaciones;
    }

    public VigenciaDTO getVigencia() {
        return vigencia;
    }

    public void setVigencia(VigenciaDTO vigencia) {
        this.vigencia = vigencia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
