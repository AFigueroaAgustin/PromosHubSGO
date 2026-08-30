package com.promohub.backend.controller;

import com.promohub.backend.dto.PromocionDTO;
import com.promohub.backend.model.Categoria;
import com.promohub.backend.model.Promocion;
import com.promohub.backend.service.IPromocionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PROMOCIONES", description = "Operaciones para obtener y filtrar Promociones")
@RestController
@RequestMapping("/api/v1/promociones")
public class PromocionController {

    private final IPromocionService promoService;

    // inyeccion de dependencia mediante constructor
    public PromocionController(IPromocionService promoService) {
        this.promoService = promoService;
    }

    @Operation(summary = "Crea las promociones recibida en el body")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Promocion guardada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Error a procesar la promocion.")})
    @PostMapping
    public ResponseEntity<String> crearPromocion(@Valid @RequestBody PromocionDTO dto) {
        Promocion promonueva = promoService.registrarDesdeDTO(dto);
        if (promonueva != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Promocion '" + promonueva.getTitulo() + "' guardada exitosamente");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al procesar la promoción.");
    }

    // GET CON FILTRADO
    @Operation(
            summary = "Listar promociones con filtros y paginación",
            description = "Devuelve una lista paginada de promociones. Permite filtrar opcionalmente por ID de banco, por Categoría comercial, o por ambos a la vez."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta ejecutada con éxito."),
            @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda o paginación inválidos.")
    })
    @GetMapping
    public ResponseEntity<Page<Promocion>> obtenerPromociones(@Parameter(
                                                                      description = "ID del banco emisor para filtrar",
                                                                      example = "1",
                                                                      required = false)
                                                                  @RequestParam(required = false) Long bancoId,
                                                              @Parameter(
                                                                      description = "Categoría comercial de la promoción",
                                                                      example = "SUPERMERCADOS", // O un valor real que tenga tu enum Categoria
                                                                      required = false)
                                                              @RequestParam(required = false) Categoria categoria,
                                                              @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
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

    @Operation(summary = "Obtiene por ID la promocion", description = "Recibe el ID en el path de la URL y devuelve la promocion si existe, si no existe devuelve 404.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Promocion encontrada"),
            @ApiResponse(responseCode = "404", description = "Promocion no encontrada con ese ID")})
    @GetMapping("/{id}")
    public ResponseEntity<Promocion> traerPromocion(
            @Parameter(
                    description = "ID de la promocion a buscar",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {
        Promocion promo = promoService.buscarPromocion(id);
        if (promo != null) {
            return ResponseEntity.ok(promo);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


}
