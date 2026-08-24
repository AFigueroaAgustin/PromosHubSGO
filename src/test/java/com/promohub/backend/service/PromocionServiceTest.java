package com.promohub.backend.service;

import com.promohub.backend.dto.PromocionDTO;
import com.promohub.backend.dto.VigenciaDTO;
import com.promohub.backend.model.Banco;
import com.promohub.backend.model.Categoria;
import com.promohub.backend.model.Comercio;
import com.promohub.backend.model.Promocion;
import com.promohub.backend.repository.IPromocionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PromocionServiceTest {

    @Mock
    private IPromocionRepository promoRepo;

    @Mock
    private IBancoService bancoService;

    @Mock
    private IComercioService comercioService;

    @InjectMocks
    private PromocionService promoService;

    @Test
    @DisplayName("Debe retornar lista de promociones paginadas")
    void traerPromocionesTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Promocion p1 = crearPromocionMock(1L, "Jueves de Vea");
        Promocion p2 = crearPromocionMock(2L, "Viernes de ChangoMas");
        Page<Promocion> pagina = new PageImpl<>(List.of(p1, p2));
        when(promoRepo.findAll(pageable)).thenReturn(pagina);

        Page<Promocion> resultado = promoService.traerPromociones(pageable);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.getContent().get(0).getTitulo()).isEqualTo("Jueves de Vea");
        verify(promoRepo, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Debe buscar promocion por ID")
    void buscarPromocionTest() {
        Promocion p = crearPromocionMock(1L, "Jueves de Vea");
        when(promoRepo.findById(1L)).thenReturn(Optional.of(p));

        Promocion resultado = promoService.buscarPromocion(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(promoRepo).findById(1L);
    }

    @Test
    @DisplayName("Debe registrar promocion exitosamente desde DTO")
    void registrarDesdeDTOTest() {
        PromocionDTO dto = new PromocionDTO();
        dto.setEntidad("Tarjeta SOL");
        dto.setTitulo("TODOS LOS JUEVES");
        dto.setCategoria("SUPERMERCADOS");
        dto.setDescripcion("4 cuotas sin interés + 20% OFF");
        dto.setDiasAplicacion("Jueves");
        dto.setComerciosAdheridos(List.of("VEA"));

        VigenciaDTO vigencia = new VigenciaDTO();
        vigencia.setInicio("2026-01-01");
        vigencia.setFin("2026-12-31");
        dto.setVigencia(vigencia);

        Banco bancoMock = new Banco();
        bancoMock.setId(1L);
        bancoMock.setNombre("Tarjeta SOL");

        Comercio comercioMock = new Comercio(1L, "VEA", Categoria.SUPERMERCADOS, "Av. Belgrano", "Santiago del Estero", null, null);

        when(bancoService.buscarPorCod(dto.getEntidad())).thenReturn(bancoMock);
        when(comercioService.buscarOCrear("VEA", Categoria.SUPERMERCADOS)).thenReturn(comercioMock);
        when(promoRepo.findByBancoAndCategoriaAndTitulo(any(), any(), any())).thenReturn(Optional.empty());
        when(promoRepo.save(any(Promocion.class))).thenAnswer(i -> {
            Promocion guardada = i.getArgument(0);
            guardada.setId(10L);
            return guardada;
        });

        Promocion resultado = promoService.registrarDesdeDTO(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(10L);
        assertThat(resultado.getTitulo()).isEqualTo("TODOS LOS JUEVES");
        assertThat(resultado.getDiasAplicacion()).isEqualTo("Jueves");
        verify(promoRepo).save(any(Promocion.class));
    }

    @Test
    @DisplayName("Debe buscar promociones por banco paginadas")
    void buscarPorBancoTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Promocion p = crearPromocionMock(1L, "Promo Banco");
        Page<Promocion> pagina = new PageImpl<>(List.of(p));

        when(promoRepo.findByBancoId(1L, pageable)).thenReturn(pagina);

        Page<Promocion> resultado = promoService.buscarPorBanco(1L, pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(1);
        verify(promoRepo).findByBancoId(1L, pageable);
    }

    @Test
    @DisplayName("Debe buscar promociones por banco y categoria paginadas")
    void buscarPorBancoYCategoriaTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Promocion p = crearPromocionMock(1L, "Promo Supermercado Banco");
        Page<Promocion> pagina = new PageImpl<>(List.of(p));

        when(promoRepo.findByBancoIdAndCategoria(1L, Categoria.SUPERMERCADOS, pageable)).thenReturn(pagina);

        Page<Promocion> resultado = promoService.buscarPorBancoYCategoria(1L, Categoria.SUPERMERCADOS, pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(1);
        verify(promoRepo).findByBancoIdAndCategoria(1L, Categoria.SUPERMERCADOS, pageable);
    }

    private Promocion crearPromocionMock(Long id, String titulo) {
        Banco banco = new Banco();
        banco.setId(1L);
        banco.setNombre("Banco Mock");
        return new Promocion(id, titulo, "Descripcion", 20.0, 5000.0,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31),
                "Jueves", Categoria.SUPERMERCADOS, banco, List.of());
    }
}

