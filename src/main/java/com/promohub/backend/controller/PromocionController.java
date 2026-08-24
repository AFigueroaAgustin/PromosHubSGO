package com.promohub.backend.controller;

import com.promohub.backend.dto.PromocionDTO;
import com.promohub.backend.model.Categoria;
import com.promohub.backend.model.Promocion;
import com.promohub.backend.service.IPromocionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promociones")
public class PromocionController {

    private final IPromocionService promoService;

    // inyeccion de dependencia mediante constructor
    public PromocionController(IPromocionService promoService) {
        this.promoService = promoService;
    }

    @PostMapping
    public ResponseEntity<String> crearPromocion(@Valid @RequestBody PromocionDTO dto) {
        Promocion promonueva = promoService.registrarDesdeDTO(dto);
        if (promonueva != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Promocion '" + promonueva.getTitulo() + "' guardada exitosamente");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al procesar la promoción.");
    }

    // GET CON FILTRADO
    @GetMapping
    public ResponseEntity<Page<Promocion>> obtenerPromociones(@RequestParam(required = false) Long bancoId,
                                                              @RequestParam(required = false) Categoria categoria,
                                                              @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){
        Page<Promocion> resultado;
        if (bancoId != null && categoria != null) {
            resultado = promoService.buscarPorBancoYCategoria(bancoId, categoria, pageable);
        } else if (bancoId != null) {
            resultado = promoService.buscarPorBanco(bancoId, pageable);
        } else if (categoria != null) {
            resultado = promoService.buscarPorCategoria(categoria, pageable);
        } else {
            resultado = promoService.traerPromociones(pageable);
        }
        return ResponseEntity.ok(resultado);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Promocion> traerPromocion(@PathVariable Long id) {
        Promocion promo = promoService.buscarPromocion(id);
        if (promo != null) {
            return ResponseEntity.ok(promo);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


}
