
package com.promohub.backend.repository;

import com.promohub.backend.model.Promocion;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Long>{
    
    public boolean existsByEntidadAndTituloAndDescripcion(String entidad,String titulo,String descrip );
    
    public List<Promocion> findByCategoria(String categoria);
    
    @Query("SELECT p FROM Promocion p WHERE (:categoria IS NULL OR p.categoria=:categoria) AND (:fecha IS NULL OR (p.fechaInicio<= :fecha AND p.fechaFin>= :fecha))")
    public Page<Promocion> busquedaDinamica(@Param("categoria")String categoria,@Param("fecha")LocalDate fecha,Pageable pageable);
    
}
