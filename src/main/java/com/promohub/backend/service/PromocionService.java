package com.promohub.backend.service;

import com.promohub.backend.dto.PromocionDTO;
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
    public List<Promocion> obtenerTodas() {
        return promocionRepository.findAll();
    }

    @Transactional
    public Promocion guardarPromocion(PromocionDTO dto) {
        logger.info("Intentando guardar promoción: {}", dto.getTitulo());
        
        boolean existe = promocionRepository.existsByEntidadAndTituloAndDescripcion(dto.getEntidad(), dto.getTitulo(), dto.getDescripcion());
        if (!existe) {//si No existe se guarda
            Promocion promo = new Promocion();
            //Strings
            promo.setEntidad(dto.getEntidad());
            promo.setTitulo(dto.getTitulo());
            promo.setCategoria(dto.getCategoria());
            promo.setDescripcion(dto.getDescripcion());
            //List
            promo.setComercios(dto.getComerciosAdheridos());

            //LocalDate
            if (dto.getVigencia() != null) {
                if (dto.getVigencia().getInicio() != null) {
                    LocalDate fInicio = LocalDate.parse(dto.getVigencia().getInicio());
                    promo.setFechaInicio(fInicio);
                }
                if (dto.getVigencia().getFin() != null) {
                    LocalDate fFin = LocalDate.parse(dto.getVigencia().getFin());
                    promo.setFechaFin(fFin);
                }
            }
            promocionRepository.save(promo);
            logger.info("Promoción guardada con ID: {}", promo.getId());
            return promo;
        } else {
            logger.warn("La promoción '{}' ya existe. Saltando...", dto.getTitulo());
           throw new IllegalStateException("La promo ya existe en la base de datos.");
        }

    }
    
    
    public Page<Promocion> filtrarPromociones(String categoria, Boolean vigente,Pageable pageable){
        LocalDate fechaFiltro =(Boolean.TRUE.equals(vigente))? LocalDate.now(): null;
        String categoriaFiltro = (categoria == null || categoria.isEmpty()) ? null : categoria;
        
        return promocionRepository.busquedaDinamica(categoriaFiltro, fechaFiltro,pageable);
    }

}
