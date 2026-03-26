package com.promohub.backend.web;

import com.promohub.backend.dto.PromocionDTO;
import com.promohub.backend.model.Promocion;
import com.promohub.backend.service.PromocionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final PromocionService promocionService;

    // Inyeccion de depencendia por contructor
    public HomeController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @GetMapping("/") //Home
    public String home(@RequestParam(required = false) String categoria,
                       @RequestParam(required = false) Boolean esvigente,
                       Model model,Pageable pageable) {


        Page<PromocionDTO> promociones = promocionService.filtrarPromociones(categoria, esvigente,pageable);
        
        model.addAttribute("promociones", promociones);
        model.addAttribute("totalPages",promociones.getTotalPages());
        model.addAttribute("currentPage",promociones.getNumber());
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("vigenteHoy", esvigente);
        return "index";
    }

}
