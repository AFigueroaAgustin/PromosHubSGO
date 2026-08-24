package com.promohub.backend.repository;

import com.promohub.backend.model.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBancoRepository extends JpaRepository<Banco, Long> {


    Optional<Banco> findByCodigoIdentificador(String codigoIdentificador);

    Optional<Banco> findByNombre(String nombre);
}
