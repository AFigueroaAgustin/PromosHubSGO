package com.promohub.backend.service;

import com.promohub.backend.model.Promocion;
import com.promohub.backend.repository.PromocionRepository;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PromocionServiceTest {

    @Mock // Dependencia simulada
    private PromocionRepository repositoryMock;

    @InjectMocks //clase bajo prueba (Test)
    private PromocionService servicioPromo;

    @Test
    @DisplayName("Debe retornar todas las promociones en una lista de tipo Promocion")
    void obtenerTodasTest() {
        //ARRANGE
        List<Promocion> listaFalsa = List.of(crearPromocionMock(1L, "Descuento lunes"), crearPromocionMock(2L, "Super Descuento BBVA"));

        when(repositoryMock.findAll()).thenReturn(listaFalsa); // cuando se utilice o se llame a "RepositoryMock.findAll()" vas a devolver "listafalsa"

        //ACT
        List<Promocion> resultado = servicioPromo.obtenerTodas();

        //ASSERT
        Assertions.assertThat(resultado).hasSize(2);
        Assertions.assertThat(resultado.get(0).getTitulo()).isEqualTo("Descueto lunes");

        verify(repositoryMock, times(1)).findAll();

    }

    @Test
    @DisplayName("Debe retornar una lista vacía si no existen promociones")
    void obtenerTodasConListaVaciaTest() {
        List<Promocion> listaVacia = List.of();

        when(repositoryMock.findAll()).thenReturn(listaVacia);

        List<Promocion> resultado = servicioPromo.obtenerTodas();

        Assertions.assertThat(resultado).isNotNull().isEmpty();
        verify(repositoryMock).findAll();
    }

    
    
    
    private Promocion crearPromocionMock(Long id, String titulo) {
        return new Promocion(
                id,
                "Banco Test", // entidad (valor por defecto)
                titulo, // titulo (variable)
                "Comida", // categoria (valor por defecto)
                "Descripcion genérica", // descripcion
                LocalDate.of(2026, 1, 1), // fechaInicio
                LocalDate.now().plusDays(7), // fechaFin
                List.of("McDonalds", "Burger King")); // comercios (lista rápida));

    }

}
