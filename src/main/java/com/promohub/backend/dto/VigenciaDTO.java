package com.promohub.backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
public class VigenciaDTO {
    @NotBlank
    private String inicio;
    @NotBlank
    private String fin;

    @JsonProperty("texto_original")
    private String textoOriginal;

    // GET Y SET
    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    
}


