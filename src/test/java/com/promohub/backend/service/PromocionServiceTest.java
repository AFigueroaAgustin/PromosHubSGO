package com.promohub.backend.service;

import com.promohub.backend.dto.PromocionDTO;
import com.promohub.backend.dto.VigenciaDTO;
import com.promohub.backend.dto.response.PageResponse;
import com.promohub.backend.exception.DuplicateResourceException;
import com.promohub.backend.exception.InvalidFechaException;
import com.promohub.backend.exception.ResourceNotFoundException;
import com.promohub.backend.model.Promocion;
import com.promohub.backend.repository.PromocionRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class PromocionServiceTest {

    @Mock // Dependencia simulada
    private PromocionRepository repositoryMock;

    @InjectMocks //clase bajo prueba (Test)
    private PromocionService servicioPromo;

    // TEST DE OBTENER PROMOCIONES
    @Test
    @DisplayName("Debe retornar todas las promociones en una lista de tipo Promocion")
    void obtenerTodasTest() {
        //ARRANGE
        List<Promocion> listaFalsa = List.of(crearPromocionMock(1L, "Descuento lunes"), crearPromocionMock(2L, "Super Descuento BBVA"));

        when(repositoryMock.findAll()).thenReturn(listaFalsa); // cuando se utilice o se llame a "RepositoryMock.findAll()" vas a devolver "listafalsa"

        //ACT
        List<PromocionDTO> resultado = servicioPromo.obtenerTodas();

        //ASSERT
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Descuento lunes");

        verify(repositoryMock, times(1)).findAll();

    }

    @Test
    @DisplayName("Debe retornar una lista vacía si no existen promociones")
    void obtenerTodasConListaVaciaTest() {
        List<Promocion> listaVacia = List.of();

        when(repositoryMock.findAll()).thenReturn(listaVacia);

        List<PromocionDTO> resultado = servicioPromo.obtenerTodas();

        assertThat(resultado).isNotNull().isEmpty();
        verify(repositoryMock).findAll();
    }

    // TEST DE GUARDAR PROMOCIONES
    @Test
    @DisplayName("Debe guardar la promoción exitosamente cuando no existe previamente")
    void guardarPromocionNuevaTest() {
        //ARRANGE
        String entidad = "Banco Nacion";
        String titulo = "Pasajes baratos";
        PromocionDTO dto = creacionDTOMock(entidad, titulo);

        when(repositoryMock.findByEntidadAndTitulo(dto.getEntidad(), dto.getTitulo())).thenReturn(Optional.empty());

        // Como el servicio crea el objeto internamente, usamos 'any(Promocion.class)'.
        when(repositoryMock.save(any(Promocion.class))).thenAnswer((iom) -> {
            Promocion promoguardada = iom.getArgument(0); // Agarra el objeto que le pasaron al save
            promoguardada.setId(10L); // Le asignamos un ID ficticio
            return promoguardada;
        });

        //ACT
        PromocionDTO resultado = servicioPromo.guardarPromocion(dto);

        //ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(10L);
        assertThat(resultado.getTitulo()).isEqualTo(titulo); // Se verifica que tomo el titulo del DTO
        assertThat(resultado.getEntidad()).isEqualTo(entidad);

        verify(repositoryMock).findByEntidadAndTitulo(
                dto.getEntidad(), dto.getTitulo());
        verify(repositoryMock, times(1)).save(any(Promocion.class)); // si llamos una sola vez al repositorio
    }

    @Test
    @DisplayName("Debe guardar o actualizar la promoción y llamar una sola vez al metodo save ")
    void guardarPromocionExistenteTest() {
        //ARRANGE
        String entidad = "Banco Nacion";
        String titulo = "Pasajes baratos";
        PromocionDTO dto = creacionDTOMock(entidad, titulo);
        Promocion promoVieja=crearPromocionMock(1L,titulo);

        when(repositoryMock.findByEntidadAndTitulo(promoVieja.getEntidad(), promoVieja.getTitulo())).thenReturn(Optional.of(promoVieja));

        when(repositoryMock.save(any(Promocion.class))).thenAnswer((iom) -> {
            Promocion promoguardada = iom.getArgument(0); // Agarra el objeto que le pasaron al save
            promoguardada.setId(1L); // Le asignamos un ID ficticio
            return promoguardada;
        });


        //ACT
        PromocionDTO resultado = servicioPromo.guardarPromocion(dto);

        //ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(promoVieja.getId());
        assertThat(resultado.getDescripcion()).isEqualTo(dto.getDescripcion());

        verify(repositoryMock,times(1)).save(any(Promocion.class));
    }

    @Test
    @DisplayName("Debe guardar la promoción con las fechas convertidas correctamente")
    void guardarPromocionConFechasConvertidasTest() {
        // ARRANGE
        String entidad = "Banco Nacion";
        String titulo = "Promo con fechas";
        PromocionDTO dto = creacionDTOMock(entidad, titulo);

        when(repositoryMock.findByEntidadAndTitulo(
                dto.getEntidad(), dto.getTitulo()))
                .thenReturn(Optional.empty());

        when(repositoryMock.save(any(Promocion.class))).thenAnswer(iom -> {
            Promocion promoGuardada = iom.getArgument(0);
            promoGuardada.setId(5L);
            return promoGuardada;
        });

        // ACT
        PromocionDTO resultado = servicioPromo.guardarPromocion(dto);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(5L);

        verify(repositoryMock).save(argThat(promo ->
                promo.getFechaInicio().equals(LocalDate.of(2026, 2, 1)) &&
                        promo.getFechaFin().equals(LocalDate.of(2026, 2, 28))
        ));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando las fechas son null o vacías")
    void guardarPromocionFechasInvalidasTest() {
        // ARRANGE
        String entidad = "Banco Nacion";
        String titulo = "Promo sin fechas";

        PromocionDTO dto = creacionDTOMock(entidad, titulo);

        dto.setVigencia(null);


        when(repositoryMock.findByEntidadAndTitulo(
                dto.getEntidad(),
                dto.getTitulo())).thenReturn(Optional.empty());

        // ACT + ASSERT
        InvalidFechaException ex=assertThrows(InvalidFechaException.class,
                () -> servicioPromo.guardarPromocion(dto)
        );
        assertThat(ex.getMessage()).isEqualTo("La promocion debe tener fecha de inicio y fecha de fin");
        // ASSERT EXTRA
        verify(repositoryMock, never()).save(any(Promocion.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la fecha inicio es null o vacía")
    void guardarPromocionFechaIncioInvalidaTest() {
        // ARRANGE
        String entidad = "Banco Nacion";
        String titulo = "Promo sin fechas";

        PromocionDTO dto = creacionDTOMock(entidad, titulo);

        dto.getVigencia().setInicio(null);


        when(repositoryMock.findByEntidadAndTitulo(
                dto.getEntidad(),
                dto.getTitulo())).thenReturn(Optional.empty());

        // ACT + ASSERT
        InvalidFechaException ex=assertThrows(InvalidFechaException.class,
                () -> servicioPromo.guardarPromocion(dto)
        );
        assertThat(ex.getMessage()).isEqualTo("La promocion debe tener fecha de inicio y fecha de fin");
        // ASSERT EXTRA
        verify(repositoryMock, never()).save(any(Promocion.class));
    }
    @Test
    @DisplayName("Debe lanzar excepción cuando la fecha fin es null o vacía")
    void guardarPromocionFechaFinInvalidaTest() {
        // ARRANGE
        String entidad = "Banco Nacion";
        String titulo = "Promo sin fechas";

        PromocionDTO dto = creacionDTOMock(entidad, titulo);

        dto.getVigencia().setFin(null);


        when(repositoryMock.findByEntidadAndTitulo(
                dto.getEntidad(),
                dto.getTitulo())).thenReturn(Optional.empty());

        // ACT + ASSERT
        InvalidFechaException ex=assertThrows(InvalidFechaException.class,
                () -> servicioPromo.guardarPromocion(dto)
        );
        assertThat(ex.getMessage()).isEqualTo("La promocion debe tener fecha de inicio y fecha de fin");
        // ASSERT EXTRA
        verify(repositoryMock, never()).save(any(Promocion.class));
    }

    // TEST DE BUSCAR PROMOCIONES POR ID
    @Test
    @DisplayName("Debe retornar la promocion con el mismo ID")
    void busquedaPromocionPorId(){
        //ARRANGUE
        when(repositoryMock.findById(1L))
                .thenReturn(Optional.of
                        (crearPromocionMock(1L, "Descuento lunes")));
        //ACT
        PromocionDTO resultado= servicioPromo.getById(1L);

        //ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getTitulo()).isEqualTo("Descuento lunes");

        verify(repositoryMock,times(1)).findById(1L);
    }

    @Test
    @DisplayName("No encuetra la promocion y debe lanzar una excepcion")
    void buscarPromocionInexistente(){
        //ARRANGUE
        when(repositoryMock.findById(99L)).thenReturn(Optional.empty());

        //ASSERT
        ResourceNotFoundException ex=assertThrows(
                ResourceNotFoundException.class,() -> servicioPromo.getById(99L));
        assertThat(ex.getMessage()).isEqualTo("Promocion con el ID " + 99L + " no encontrado");
    }

    // TEST DE FILTRAR PROMOCIONES
    @Test
    @DisplayName("Debe retornar todas las promociones filtradas por categoria y fecha")
    void filtrarpromocionesTest(){
        // ARRANGE
        List<Promocion> listaFalsa = List.of(crearPromocionMock(1L, "Descuento lunes"), crearPromocionMock(2L, "Super Descuento BBVA"));
        Page<Promocion> paginaFalsa= new PageImpl<>(listaFalsa);
        Pageable pageable = PageRequest.of(0, 10);
        when(repositoryMock.busquedaDinamica("Comida", LocalDate.now(), pageable))
                .thenReturn(paginaFalsa);
        // ACT
        Page<PromocionDTO> resultado=servicioPromo.filtrarPromociones("Comida", true, pageable);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(2);
        assertThat(resultado.getContent().get(0).getTitulo()).isEqualTo("Descuento lunes");

        verify(repositoryMock).busquedaDinamica("Comida",LocalDate.now(),pageable); // Verifica que se llamo una vez (esta es la forma)

    }
    @Test
    @DisplayName("Debe retornar todas las promociones filtradas por categoria")
    void filtrarpromocionesSinFechaTest(){
        // ARRANGE
        List<Promocion> listaFalsa = List.of(crearPromocionMock(1L, "Descuento lunes"), crearPromocionMock(2L, "Super Descuento BBVA"));
        Page<Promocion> paginaFalsa= new PageImpl<>(listaFalsa);
        Pageable pageable = PageRequest.of(0, 10);
        when(repositoryMock.busquedaDinamica("Comida", null, pageable))
                .thenReturn(paginaFalsa);
        // ACT
        Page<PromocionDTO> resultado=servicioPromo.filtrarPromociones("Comida", false, pageable);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(2);
        assertThat(resultado.getContent().get(0).getTitulo()).isEqualTo("Descuento lunes");

        verify(repositoryMock).busquedaDinamica("Comida",null,pageable); // Verifica que se llamo una vez (esta es la forma)

    }
    @Test
    @DisplayName("Debe retornar todas las promociones filtradas")
    void filtrarpromocionesSinCategoriaTest(){
        // ARRANGE
        List<Promocion> listaFalsa = List.of(crearPromocionMock(1L, "Descuento lunes"), crearPromocionMock(2L, "Super Descuento BBVA"));
        Page<Promocion> paginaFalsa= new PageImpl<>(listaFalsa);
        Pageable pageable = PageRequest.of(0, 10);
        when(repositoryMock.busquedaDinamica(null, LocalDate.now(), pageable))
                .thenReturn(paginaFalsa);
        // ACT
        Page<PromocionDTO> resultado=servicioPromo.filtrarPromociones(null, true, pageable);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(2);
        assertThat(resultado.getContent().get(0).getTitulo()).isEqualTo("Descuento lunes");

        verify(repositoryMock).busquedaDinamica(null,LocalDate.now(),pageable); // Verifica que se llamo una vez (esta es la forma)

    }
    @Test
    @DisplayName("Debe retornar todas las promociones filtradas")
    void filtrarpromocionesSinFechaSinCategoriaTest(){
        // ARRANGE
        List<Promocion> listaFalsa = List.of(crearPromocionMock(1L, "Descuento lunes"), crearPromocionMock(2L, "Super Descuento BBVA"));
        Page<Promocion> paginaFalsa= new PageImpl<>(listaFalsa);
        Pageable pageable = PageRequest.of(0, 10);
        when(repositoryMock.busquedaDinamica(null, null, pageable))
                .thenReturn(paginaFalsa);
        // ACT
        Page<PromocionDTO> resultado=servicioPromo.filtrarPromociones(" ", null, pageable);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(2);
        assertThat(resultado.getContent().get(0).getTitulo()).isEqualTo("Descuento lunes");

        verify(repositoryMock).busquedaDinamica(null,null,pageable); // Verifica que se llamo una vez (esta es la forma)

    }



    // AUX
    private Promocion crearPromocionMock(Long id, String titulo) {
        return new Promocion(
                id,
                "Banco Nacion", // entidad (valor por defecto)
                titulo, // titulo (variable)
                "Comida", // categoria (valor por defecto)
                "Descripcion genérica", // descripcion
                LocalDate.of(2026, 1, 1), // fechaInicio
                LocalDate.now().plusDays(7), // fechaFin
                List.of("McDonalds", "Burger King")); // comercios (lista rápida));

    }

    private PromocionDTO creacionDTOMock(String entidad, String titulo) {
        PromocionDTO dto = new PromocionDTO();
        dto.setEntidad(entidad);
        dto.setTitulo(titulo);
        dto.setCategoria("Viajes");
        dto.setDescripcion("Descuento en vuelos");
        dto.setComerciosAdheridos(List.of("Aerolineas", "FlyBondi"));

        VigenciaDTO vigencia = new VigenciaDTO();
        vigencia.setInicio("2026-02-01");
        vigencia.setFin("2026-02-28");

        dto.setVigencia(vigencia);

        return dto;
    }


}
