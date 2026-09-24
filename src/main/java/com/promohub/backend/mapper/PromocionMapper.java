package com.promohub.backend.mapper;

import com.promohub.backend.dto.response.BancoResponseDTO;
import com.promohub.backend.dto.response.ComercioResponseDTO;
import com.promohub.backend.dto.response.PromocionResponseDTO;
import com.promohub.backend.model.Banco;
import com.promohub.backend.model.Comercio;
import com.promohub.backend.model.Promocion;

import java.util.Collections;
import java.util.List;

public final class PromocionMapper {

    private PromocionMapper() {
    }

    public static PromocionResponseDTO toDTO(Promocion promo) {
        if (promo == null) {
            return null;
        }

        return PromocionResponseDTO.builder()
                .id(promo.getId())
                .titulo(promo.getTitulo())
                .descripcion(promo.getDescripcion())
                .porcentajeDescuento(promo.getPorcentajeDescuento())
                .topeReintegro(promo.getTopeReintegro())
                .fechaInicio(promo.getFechaInicio())
                .fechaFin(promo.getFechaFin())
                .diasAplicacion(promo.getDiasAplicacion())
                .categoria(promo.getCategoria())
                .activa(promo.isVigente())
                .banco(toBancoDTO(promo.getBanco()))
                .comercios(toComercioDTOList(promo.getComercios()))
                .build();
    }

    public static BancoResponseDTO toBancoDTO(Banco banco) {
        if (banco == null) {
            return null;
        }
        return BancoResponseDTO.builder()
                .id(banco.getId())
                .nombre(banco.getNombre())
                .codigoIdentificador(banco.getCodigoIdentificador())
                .urlLogo(banco.getUrlLogo())
                .sitioWeb(banco.getSitioWeb())
                .tipoEmisor(banco.getTipoEmisor())
                .build();
    }

    public static ComercioResponseDTO toComercioDTO(Comercio comercio) {
        if (comercio == null) {
            return null;
        }
        return ComercioResponseDTO.builder()
                .id(comercio.getId())
                .nombre(comercio.getNombre())
                .categoria(comercio.getCategoria())
                .direccion(comercio.getDireccion())
                .ciudad(comercio.getCiudad())
                .build();
    }

    public static List<ComercioResponseDTO> toComercioDTOList(List<Comercio> comercios) {
        if (comercios == null || comercios.isEmpty()) {
            return Collections.emptyList();
        }
        return comercios.stream()
                .map(PromocionMapper::toComercioDTO)
                .toList();
    }
}
