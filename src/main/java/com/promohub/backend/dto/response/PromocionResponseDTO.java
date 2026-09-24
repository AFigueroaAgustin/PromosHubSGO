package com.promohub.backend.dto.response;

import com.promohub.backend.model.Categoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromocionResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Double porcentajeDescuento;
    private Double topeReintegro;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String diasAplicacion;
    private Categoria categoria;
    private boolean activa;
    private BancoResponseDTO banco;
    private List<ComercioResponseDTO> comercios;
}
