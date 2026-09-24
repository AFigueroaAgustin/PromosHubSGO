package com.promohub.backend.dto.response;

import com.promohub.backend.model.Categoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComercioResponseDTO {
    private Long id;
    private String nombre;
    private Categoria categoria;
    private String direccion;
    private String ciudad;
}
