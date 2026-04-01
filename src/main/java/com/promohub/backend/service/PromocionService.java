package com.promohub.backend.service;

import com.promohub.backend.dto.PromocionDTO;
import com.promohub.backend.dto.VigenciaDTO;
import com.promohub.backend.exception.DuplicateResourceException;
import com.promohub.backend.exception.InvalidFechaException;
import com.promohub.backend.exception.ResourceNotFoundException;
import com.promohub.backend.model.Promocion;
import com.promohub.backend.repository.PromocionRepository;

import java.time.LocalDate;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PromocionService {

    private final PromocionRepository promocionRepository;
    private static final Logger logger = LoggerFactory.getLogger(PromocionService.class);


    // inyeccion de dependencia mediante constructor
    public PromocionService(PromocionRepository promocionRepository) {
        this.promocionRepository = promocionRepository;
    }

    //Logica
    public List<PromocionDTO> obtenerTodas() {
        return promocionRepository.findAll().stream().map(this::toDTO).toList();
    }

    public PromocionDTO getById(Long id) {
        PromocionDTO resultado = promocionRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Promocion", id));
        return resultado;
    }

    @Transactional
    public PromocionDTO guardarPromocion(PromocionDTO dto) {
        logger.info("Intentando guardar promoción: {}", dto.getTitulo());

        validarNoDuplicado(dto);

        //si No existe se guarda
            Promocion promo = new Promocion();
            //Strings
            promo.setEntidad(dto.getEntidad());
            promo.setTitulo(dto.getTitulo());
            promo.setCategoria(dto.getCategoria());
            promo.setDescripcion(dto.getDescripcion());
            //List
            promo.setComercios(dto.getComerciosAdheridos());

            //LocalDate
            RangoFechas fechas = convertirALocalDate(dto);
            promo.setFechaInicio(fechas.inicio());
            promo.setFechaFin(fechas.fin());

            Promocion guardada = promocionRepository.save(promo);
            logger.info("Promoción guardada con ID: {}", guardada.getId());
            return toDTO(guardada);

    }

    public Page<PromocionDTO> filtrarPromociones(String categoria, Boolean vigente, Pageable pageable) {
        LocalDate fechaFiltro = (Boolean.TRUE.equals(vigente)) ? LocalDate.now() : null;
        String categoriaFiltro = (categoria == null || categoria.isBlank()) ? null : categoria;

        return promocionRepository.busquedaDinamica(categoriaFiltro, fechaFiltro, pageable).map(this::toDTO); //convierte cada promocion en DTO

    }

    private void validarNoDuplicado(PromocionDTO dto) {
        boolean existe = promocionRepository.existsByEntidadAndTituloAndDescripcion(
                dto.getEntidad(),
                dto.getTitulo(),
                dto.getDescripcion()
        );

        if (existe) {
            logger.warn("La promoción '{}' ya existe. Saltando...", dto.getTitulo());
            throw new DuplicateResourceException("La promocion", dto.getTitulo());
        }
    }

    private record RangoFechas(LocalDate inicio, LocalDate fin) { // Se utliza para transportar los datos de las fechas

    }

    private RangoFechas convertirALocalDate(PromocionDTO dto) {

        if (dto.getVigencia() == null ||
                dto.getVigencia().getInicio() == null ||
                dto.getVigencia().getFin() == null ||
                dto.getVigencia().getInicio().isBlank() ||
                dto.getVigencia().getFin().isBlank()) {

            throw new InvalidFechaException("La promocion debe tener fecha de inicio y fecha de fin");
        }
        LocalDate inicio = LocalDate.parse(dto.getVigencia().getInicio());
        LocalDate fin = LocalDate.parse(dto.getVigencia().getFin());

        return new RangoFechas(inicio, fin);
    }

    private PromocionDTO toDTO(Promocion p) {
        PromocionDTO dto = new PromocionDTO();
        dto.setId(p.getId());
        dto.setEntidad(p.getEntidad());
        dto.setTitulo(p.getTitulo());
        dto.setCategoria(p.getCategoria());
        dto.setDescripcion(p.getDescripcion());
        dto.setComerciosAdheridos(p.getComercios());

        VigenciaDTO vigenciaDTO = new VigenciaDTO();
        vigenciaDTO.setInicio(p.getFechaInicio().toString());
        vigenciaDTO.setFin(p.getFechaFin().toString());
        dto.setVigencia(vigenciaDTO);

        return dto;
    }

}
