package com.promohub.backend.repository;

import com.promohub.backend.model.Banco;
import com.promohub.backend.model.Categoria;
import com.promohub.backend.model.Promocion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPromocionRepository extends JpaRepository<Promocion, Long> {

    Optional<Promocion> findByBancoAndCategoriaAndTitulo(Banco banco, Categoria categoria, String titulo);

    Page<Promocion> findByBancoId(Long bancoId, Pageable pageable);

    Page<Promocion> findByCategoria(Categoria categoria, Pageable pageable);

}
