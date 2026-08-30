package com.promohub.backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estructura que define la validez temporal y el texto legal de vigencia")
public class VigenciaDTO {
    @Schema(
            description = "Fecha a partir de la cual comienza a regir la promoción (Formato ISO: YYYY-MM-DD)",
            example = "2026-08-01",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "La fecha de inicio es obligatoria")
    private String inicio;

    @Schema(
            description = "Fecha límite hasta la cual es válida la promoción (Formato ISO: YYYY-MM-DD)",
            example = "2026-12-31",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "La fecha de fin es obligatoria")
    private String fin;

    @Schema(
            description = "Cadena de texto cruda extraída directamente del sitio web con los términos de vigencia",
            example = "Válido desde el 01/08/2026 hasta el 31/12/2026 inclusive o hasta agotar stock.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("texto_original")
    private String textoOriginal;

    
}


