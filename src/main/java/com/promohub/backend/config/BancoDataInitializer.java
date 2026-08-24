package com.promohub.backend.config;

import com.promohub.backend.model.Banco;
import com.promohub.backend.model.TipoEmisor;
import com.promohub.backend.repository.IBancoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BancoDataInitializer implements CommandLineRunner {

    private final IBancoRepository bancoRepository;

    public BancoDataInitializer(IBancoRepository bancoRepository) {
        this.bancoRepository = bancoRepository;
    }

    @Override
    public void run(String... args) {
        List<Banco> bancos = bancoRepository.findAll();
        for (Banco banco : bancos) {
            boolean modificado = false;
            String nombre = banco.getNombre().toUpperCase();

            // Asignar tipo de emisor si falta
            if (banco.getTipoEmisor() == null) {
                banco.setTipoEmisor(TipoEmisor.desdeTexto(nombre));
                modificado = true;
            }

            // Asignar logotipo oficial
            String logo = resolverLogo(nombre);
            if (logo != null && (banco.getUrlLogo() == null || !banco.getUrlLogo().equals(logo))) {
                banco.setUrlLogo(logo);
                modificado = true;
            }

            if (modificado) {
                bancoRepository.save(banco);
            }
        }
    }

    private String resolverLogo(String nombre) {
        if (nombre.contains("SOL")) return "/images/logos/sol.png";
        if (nombre.contains("UNICA") || nombre.contains("ÚNICA")) return "/images/logos/unica.png";
        if (nombre.contains("SUCREDITO") || nombre.contains("SUCRÉDITO")) return "/images/logos/sucredito.svg";
        if (nombre.contains("NARANJA")) return "/images/logos/naranjax.png";
        if (nombre.contains("SANTIAGO") || nombre.contains("BSE")) return "/images/logos/bse.png";
        if (nombre.contains("NACION") || nombre.contains("NACIÓN") || nombre.contains("BNA")) return "/images/logos/bna.png";
        if (nombre.contains("MACRO")) return "/images/logos/macro.png";
        if (nombre.contains("SANTANDER")) return "/images/logos/santander.png";
        if (nombre.contains("GALICIA")) return "/images/logos/galicia.png";
        if (nombre.contains("MODO")) return "/images/logos/modo.png";
        if (nombre.contains("VIUMI") || nombre.contains("VIÜMI")) return "/images/logos/viumi.png";
        return null;
    }
}
