package com.promohub.backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VigenciaDTO {
    @NotBlank(message = "La fecha de inicio es obligatoria")
    private String inicio;
    @NotBlank(message = "La fecha de fin es obligatoria")
    private String fin;

    @JsonProperty("texto_original")
    private String textoOriginal;

    
}


