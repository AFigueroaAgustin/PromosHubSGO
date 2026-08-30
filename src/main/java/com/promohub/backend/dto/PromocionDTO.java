package com.promohub.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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

@Schema(description = "Estructura de datos requerida para dar de alta una nueva promoción")

public class PromocionDTO {
    @Schema(
            description = "Identificador único de la promoción (autogenerado por la base de datos). Omitir al crear.",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
            description = "Nombre de la entidad bancaria o financiera emisora del beneficio",
            example = "Banco Santiago del Estero",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "La entidad es obligatoria")
    private String entidad;

    @Schema(
            description = "Categoría o rubro al que aplica la promoción",
            example = "Supermercados",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "La categoria es obligatoria")
    private String categoria;

    @Schema(
            description = "Título comercial o resumen principal del beneficio",
            example = "30% de reintegro con tarjeta de débito",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;

    @Schema(
            description = "Detalle extendido sobre cómo opera la promoción y condiciones de reintegro",
            example = "Tope de reintegro de $10.000 por mes por cuenta.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String descripcion;

    @Schema(
            description = "Días de la semana en los que aplica el beneficio",
            example = "Lunes y Miércoles"
    )
    @JsonProperty("dias_aplicacion")
    private String diasAplicacion;

    @Schema(
            description = "Lista de nombres de locales o cadenas comerciales donde aplica el descuento",
            example = "[\"Vea\", \"ChangoMás\", \"Carrefour\"]"
    )
    @JsonProperty("comercios_adheridos")
    private List<String> comerciosAdheridos;

    @Schema(
            description = "Términos, condiciones legales o exclusiones aplicables a la promoción",
            example = "No acumulable con otras promociones vigentes. Válido solo para consumo familiar."
    )
    @JsonProperty("legales_observaciones")
    private String legalesObservaciones;

    @Schema(
            description = "Rango de fechas en las que la promoción es válida",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "La vigencia es obligatoria")
    @Valid
    private VigenciaDTO vigencia;
}
