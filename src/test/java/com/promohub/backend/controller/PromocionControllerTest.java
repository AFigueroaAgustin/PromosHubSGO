package com.promohub.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promohub.backend.dto.PromocionDTO;
import com.promohub.backend.dto.VigenciaDTO;
import com.promohub.backend.model.Banco;
import com.promohub.backend.model.Categoria;
import com.promohub.backend.model.Promocion;
import com.promohub.backend.service.IPromocionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PromocionController.class)
public class PromocionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IPromocionService promocionService;

    @Test
    @DisplayName("Debe retornar 200 cuando el ID de la promocion existe")
    void debeRetornarPromocionCuandoIdExiste() throws Exception {
        Promocion promo = crearPromocionMock(1L, "Viaja gratis");
        when(promocionService.buscarPromocion(1L)).thenReturn(promo);

        ResultActions resultado = mockMvc.perform(get("/api/promociones/1"));

        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Viaja gratis"));

        verify(promocionService).buscarPromocion(1L);
    }

    @Test
    @DisplayName("Debe retornar 404 cuando el ID de la promocion no existe")
    void debeRetornar404CuandoIdNoExiste() throws Exception {
        when(promocionService.buscarPromocion(99L)).thenReturn(null);

        ResultActions resultado = mockMvc.perform(get("/api/promociones/99"));

        resultado.andExpect(status().isNotFound());
        verify(promocionService).buscarPromocion(99L);
    }

    @Test
    @DisplayName("Debe retornar 201 cuando guarda la promoción")
    void debeRetornar201CuandoGuarda() throws Exception {
        PromocionDTO promocionDTO = creacionDTOMock("Banco SDE", "Viaja gratis");
        Promocion promo = crearPromocionMock(1L, "Viaja gratis");
        when(promocionService.registrarDesdeDTO(any(PromocionDTO.class))).thenReturn(promo);

        String bodyJson = objectMapper.writeValueAsString(promocionDTO);

        ResultActions resultado = mockMvc.perform(post("/api/promociones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyJson));

        resultado.andExpect(status().isCreated());
        verify(promocionService).registrarDesdeDTO(any(PromocionDTO.class));
    }

    @Test
    @DisplayName("Debe retornar 200 y devolver la pagina con promociones")
    void debeRetornar200yDevolverLaPagina() throws Exception {
        List<Promocion> lista = List.of(crearPromocionMock(1L, "Descuento lunes"), crearPromocionMock(2L, "Super Descuento"));
        Page<Promocion> pagina = new PageImpl<>(lista);

        when(promocionService.traerPromociones(any(Pageable.class))).thenReturn(pagina);

        ResultActions resultado = mockMvc.perform(get("/api/promociones"));

        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].titulo").value("Descuento lunes"))
                .andExpect(jsonPath("$.totalElements").value(2));

        verify(promocionService).traerPromociones(any(Pageable.class));
    }

    private Promocion crearPromocionMock(Long id, String titulo) {
        Banco banco = new Banco();
        banco.setId(1L);
        banco.setNombre("Banco SDE");
        return new Promocion(id, titulo, "Descripcion", 20.0, 5000.0,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31),
                "Lunes", Categoria.SUPERMERCADOS, banco, List.of());
    }

    private PromocionDTO creacionDTOMock(String entidad, String titulo) {
        PromocionDTO dto = new PromocionDTO();
        dto.setEntidad(entidad);
        dto.setTitulo(titulo);
        dto.setCategoria("SUPERMERCADOS");
        dto.setDescripcion("Descuento en vuelos");
        dto.setComerciosAdheridos(List.of("VEA", "ChangoMas"));

        VigenciaDTO vigencia = new VigenciaDTO();
        vigencia.setInicio("2026-02-01");
        vigencia.setFin("2026-02-28");
        dto.setVigencia(vigencia);

        return dto;
    }
}

