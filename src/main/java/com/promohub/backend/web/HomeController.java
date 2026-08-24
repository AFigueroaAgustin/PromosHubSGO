package com.promohub.backend.web;

import com.promohub.backend.dto.PromocionDTO;
import com.promohub.backend.model.Banco;
import com.promohub.backend.model.Categoria;
import com.promohub.backend.model.Promocion;
import com.promohub.backend.service.IBancoService;
import com.promohub.backend.service.IPromocionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final IPromocionService promoService;
    private final IBancoService bancoService;

    // Inyección de dependencias por constructor
    public HomeController(IPromocionService promoService, IBancoService bancoService) {
        this.promoService = promoService;
        this.bancoService = bancoService;
    }

    @GetMapping("/") //Home
    public String home(@RequestParam(required = false) Long bancoId,
                       @RequestParam(required = false) Categoria categoria,
                       @PageableDefault(page = 0, size = 9, sort = "fechaFin", direction = Sort.Direction.DESC)
                       Model model, Pageable pageable) {
        Page<Promocion> paginaPromos;

        //Aplicamos los filtros según lo que haya seleccionado el usuario
        if (bancoId != null && categoria != null) {
            paginaPromos = promoService.buscarPorBancoYCategoria(bancoId, categoria, pageable);
        } else if (bancoId != null) {
            paginaPromos = promoService.buscarPorBanco(bancoId, pageable);
        } else if (categoria != null) {
            paginaPromos = promoService.buscarPorCategoria(categoria, pageable);
        } else {
            paginaPromos = promoService.traerPromociones(pageable);
        }
        // Cargamos los datos necesarios para la vista en el Model
        List<Banco> bancos = bancoService.traerBancos();
        Categoria[] categorias = Categoria.values(); // Todas las opciones del Enum
        model.addAttribute("promociones", paginaPromos);
        model.addAttribute("bancos", bancos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("bancoSeleccionado", bancoId);
        model.addAttribute("categoriaSeleccionada", categoria);
        //Renderizamos la plantilla index.html
        return "index";
    }

}
