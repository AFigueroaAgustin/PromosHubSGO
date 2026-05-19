package com.promohub.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promohub.backend.dto.PromocionDTO;
import com.promohub.backend.dto.VigenciaDTO;
import com.promohub.backend.exception.DuplicateResourceException;
import com.promohub.backend.exception.ResourceNotFoundException;
import com.promohub.backend.model.Promocion;
import com.promohub.backend.service.PromocionService;
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


import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(PromocionController.class)
public class PromocionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PromocionService promocionService;

    @Test
    @DisplayName("Debe retornar 200 cuando el ID de la promocion existe ")
    void debeRetornarPromocionCuandoIdExiste() throws Exception {
        // ARRANGE
        PromocionDTO promocionDTO = creacionDTOMock("Banco SDE", "Viaja gratis");
        when(promocionService.getById(1L)).thenReturn(promocionDTO);

        // ACT
        ResultActions resultado = mockMvc.perform(get("/api/promociones/1"));

        // ASSERT
        resultado.andExpect(status().isOk());
        resultado.andExpect(jsonPath("$.entidad").value("Banco SDE"));
        resultado.andExpect(jsonPath("$.titulo").value("Viaja gratis"));
        resultado.andExpect(jsonPath("$.vigencia").isNotEmpty());


        verify(promocionService).getById(1L);

    }

    @Test
    @DisplayName("Debe retornar 404 cuando el ID de la promocion no existe")
    void debeRetornar404CuandoIdNoExiste() throws Exception {
        // ARRANGE
        PromocionDTO promocionDTO = creacionDTOMock("Banco SDE", "Viaja gratis");
        when(promocionService.getById(99L)).thenThrow(new ResourceNotFoundException("Promocion", 99L));

        // ACT
        ResultActions resultado = mockMvc.perform(get("/api/promociones/99"));

        // ASSERT
        resultado.andExpect(status().isNotFound());

        resultado.andExpect(content().string("Promocion con el ID " + 99L + " no encontrado"));

        verify(promocionService).getById(99L);

    }

    @Test
    @DisplayName("Debe retornar 200 cuando guarda o actualiza la promoción")
    void debeRetornar200CuandoGuardaOActualiza() throws Exception {
        PromocionDTO promocionDTO = creacionDTOMock("Banco SDE", "Viaja gratis");
        when(promocionService.guardarPromocion(any(PromocionDTO.class))).thenReturn(promocionDTO);

        String bodyJson = objectMapper.writeValueAsString(promocionDTO);

        // ACT
        ResultActions resultado = mockMvc.perform(post("/api/promociones").contentType(MediaType.APPLICATION_JSON).content(bodyJson));

        // ASSERT
        resultado.andExpect(status().isOk());

        verify(promocionService).guardarPromocion(any(PromocionDTO.class));
    }


    @Test
    @DisplayName("Debe retornar 400 cuando el body de la promocion es invalido por entidad vacia")
    void debeRetornar400CuandoElBodyEsInvalidoPorEntidadBlank() throws Exception {
        // ARRANGE
        PromocionDTO promocionDTO = creacionDTOMock("", "Viaja gratis");
        String bodyJson = objectMapper.writeValueAsString(promocionDTO);

        // ACT
        ResultActions resultado = mockMvc.perform(
                post("/api/promociones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyJson)
        );

        // ASSERT
        resultado.andExpect(status().isBadRequest());

        verify(promocionService, never()).guardarPromocion(any(PromocionDTO.class));
    }

    @Test
    @DisplayName("Debe retornar 400 cuando el body de la promocion es invalido por titulo vacio")
    void debeRetornar400CuandoElBodyEsInvalidoPorTituloBlank() throws Exception {
        // ARRANGE
        PromocionDTO promocionDTO = creacionDTOMock("Banco SDE", "");
        String bodyJson = objectMapper.writeValueAsString(promocionDTO);

        // ACT
        ResultActions resultado = mockMvc.perform(
                post("/api/promociones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyJson)
        );

        // ASSERT
        resultado.andExpect(status().isBadRequest());

        verify(promocionService, never()).guardarPromocion(any(PromocionDTO.class));
    }

    @Test
    @DisplayName("Debe retornar 200 y devolver la pagina con promociones")
    void debeRetornar200yDevolverLaPagina() throws Exception {
        List<PromocionDTO> listaFalsa = List.of(creacionDTOMock("Banco SGO", "Descuento lunes"), creacionDTOMock("BBVA", "Super Descuento BBVA"));
        Page<PromocionDTO> paginaFalsa = new PageImpl<PromocionDTO>(listaFalsa);

        when(promocionService.filtrarPromociones(
                eq("Viajes"),
                eq(true),
                any(Pageable.class)
        )).thenReturn(paginaFalsa);

        ResultActions resultado = mockMvc.perform(get("/api/promociones")
                .param("categoria", "Viajes")
                .param("esvigente", "true")
                .param("page", "0")
                .param("size", "5"));

        resultado.andExpect(status().isOk());
        resultado.andExpect(jsonPath("$.content[0].titulo").value("Descuento lunes"));
        resultado.andExpect(jsonPath("$.content[0].entidad").value("Banco SGO"));
        resultado.andExpect(jsonPath("$.content[1].titulo").value("Super Descuento BBVA"));
        resultado.andExpect(jsonPath("$.content[1].entidad").value("BBVA"));
        resultado.andExpect(jsonPath("$.totalElements").value(2));
        resultado.andExpect(jsonPath("$.totalPages").value(1));
        resultado.andExpect(jsonPath("$.first").value(true));
        resultado.andExpect(jsonPath("$.last").value(true));

        verify(promocionService).filtrarPromociones(eq("Viajes"), eq(true), any(Pageable.class));
    }

    @Test
    @DisplayName("Debe retornar 200 y la pagina vacia sin promociones")
    void debeRetornar200yDevolverLaPaginaVacia() throws Exception {
        List<PromocionDTO> listaFalsa = List.of();
        Page<PromocionDTO> paginaFalsa = new PageImpl<PromocionDTO>(listaFalsa);

        when(promocionService.filtrarPromociones(
                eq("Viajes"),
                eq(true),
                any(Pageable.class)
        )).thenReturn(paginaFalsa);



        ResultActions resultado = mockMvc.perform(get("/api/promociones")
                .param("categoria", "Viajes")
                .param("esvigente", "true")
                .param("page", "0")
                .param("size", "5"));


        resultado.andExpect(status().isOk());
        resultado.andExpect(jsonPath("$.content").isEmpty());
        resultado.andExpect(jsonPath("$.totalElements").value(0));

        verify(promocionService).filtrarPromociones(eq("Viajes"), eq(true), any(Pageable.class));

    }

    @Test
    @DisplayName("Debe retornar 200 cuando no se envian los filtros")
    void debeRetornar200CuandoNoSeEnvianFiltros() throws Exception {
        List<PromocionDTO> listaFalsa = List.of(creacionDTOMock("Banco SGO", "Descuento lunes"), creacionDTOMock("BBVA", "Super Descuento BBVA"));
        Page<PromocionDTO> paginaFalsa = new PageImpl<PromocionDTO>(listaFalsa);

        when(promocionService.filtrarPromociones(
                isNull(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(paginaFalsa);



        ResultActions resultado = mockMvc.perform(get("/api/promociones"));



        resultado.andExpect(status().isOk());
        resultado.andExpect(jsonPath("$.content[0].entidad").value("Banco SGO"));
        resultado.andExpect(jsonPath("$.totalElements").value(2));

        verify(promocionService).filtrarPromociones(isNull(), isNull(), any(Pageable.class));

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
