package com.promohub.backend.controller;

import com.promohub.backend.dto.PromocionDTO;
import com.promohub.backend.dto.response.PageResponse;
import com.promohub.backend.service.PromocionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promociones")
public class PromocionController {

    private final PromocionService promocionService;

    // inyeccion de dependencia mediante constructor
    public PromocionController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    //endpoints
    @GetMapping
    public ResponseEntity<PageResponse<PromocionDTO>> obtenerPromociones(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Boolean esvigente,
            @PageableDefault(sort = "fechaFin") Pageable pageable) {

        Page<PromocionDTO> page=promocionService.filtrarPromociones(categoria, esvigente,pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }
    // GET /api/promociones/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PromocionDTO> obtenerPromocionesPorId(@PathVariable Long id) {
        return ResponseEntity.ok(promocionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PromocionDTO> crearPromocion(@Valid @RequestBody PromocionDTO dto) {
            PromocionDTO creada = promocionService.guardarPromocion(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);

}
}
