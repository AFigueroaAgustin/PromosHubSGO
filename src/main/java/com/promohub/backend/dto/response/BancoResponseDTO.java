package com.promohub.backend.dto.response;

import com.promohub.backend.model.TipoEmisor;
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
public class BancoResponseDTO {
    private Long id;
    private String nombre;
    private String codigoIdentificador;
    private String urlLogo;
    private String sitioWeb;
    private TipoEmisor tipoEmisor;
}
