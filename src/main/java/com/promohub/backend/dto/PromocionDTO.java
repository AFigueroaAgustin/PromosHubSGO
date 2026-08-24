package com.promohub.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromocionDTO {
    private Long id;

    @NotBlank(message = "La entidad es obligatoria")
    private String entidad;

    @NotBlank(message = "La categoria es obligatoria")
    private String categoria;

    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;

    private String descripcion;

    @JsonProperty("dias_aplicacion")
    private String diasAplicacion;

    @JsonProperty("comercios_adheridos")
    private List<String> comerciosAdheridos;

    @JsonProperty("legales_observaciones")
    private String legalesObservaciones;

    @NotNull(message = "La vigencia es obligatoria")
    @Valid
    private VigenciaDTO vigencia;
}
