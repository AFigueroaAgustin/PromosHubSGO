package com.promohub.backend.controller;

import com.promohub.backend.dto.PromocionDTO;
import com.promohub.backend.model.Promocion;
import com.promohub.backend.service.PromocionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public Page<Promocion> obtenerPromociones(@RequestParam(required = false) String categoria,
            @RequestParam(required = false) Boolean esvigente,Pageable pageable) {
        Page<Promocion> promociones=promocionService.filtrarPromociones(categoria, esvigente,pageable);
        
        return promociones;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Promocion crearPromocion(@Valid @RequestBody PromocionDTO dto) {

            Promocion promoguardada = promocionService.guardarPromocion(dto);
            return promoguardada;

}
}
