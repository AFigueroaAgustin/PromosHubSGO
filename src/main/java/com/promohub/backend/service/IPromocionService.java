package com.promohub.backend.service;

import com.promohub.backend.dto.PromocionDTO;
import com.promohub.backend.model.Categoria;
import com.promohub.backend.model.Promocion;
import com.promohub.backend.repository.IPromocionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPromocionService {

    Page<Promocion> traerPromociones(Pageable pageable);

    Promocion buscarPromocion(Long id);

    Promocion crearPromocion(Promocion promocionACrear);

    boolean borrarPromocion(Long id);

    Promocion actualizarPromocion(Long id,Promocion promocionActualizar);

    // Metodos especiales del service

    Page<Promocion> buscarPorBanco(Long bancoId, Pageable pageable);

    Page<Promocion> buscarPorCategoria(Categoria categoria, Pageable pageable);

    // Para recibir el DTO de Python y usar el 'findByBancoAndTitulo' del repositorio
    Promocion registrarDesdeDTO(PromocionDTO dto);


}
