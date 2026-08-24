package com.promohub.backend.repository;

import com.promohub.backend.model.Comercio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IComercioRepository extends JpaRepository<Comercio, Long> {

    Optional<Comercio> findByNombreIgnoreCase(String nombre);
}
