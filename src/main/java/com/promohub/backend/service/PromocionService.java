package com.promohub.backend.service;

import com.promohub.backend.dto.PromocionDTO;
import com.promohub.backend.dto.VigenciaDTO;
import com.promohub.backend.exception.DuplicateResourceException;
import com.promohub.backend.exception.InvalidFechaException;
import com.promohub.backend.exception.ResourceNotFoundException;
import com.promohub.backend.model.Banco;
import com.promohub.backend.model.Categoria;
import com.promohub.backend.model.Comercio;
import com.promohub.backend.model.Promocion;
import com.promohub.backend.model.TipoEmisor;
import com.promohub.backend.repository.IComercioRepository;
import com.promohub.backend.repository.IPromocionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PromocionService implements IPromocionService {

    private final IPromocionRepository promoRepo;
    private final IComercioService comercioService;
    private final IBancoService bancoService;


    public PromocionService(IPromocionRepository promoRepo, IComercioService comercioService, IBancoService bancoService) {
        this.promoRepo = promoRepo;
        this.comercioService = comercioService;
        this.bancoService = bancoService;
    }

    //Logica
    @Override
    public Page<Promocion> traerPromociones(Pageable pageable) {
        return promoRepo.findAll(pageable);
    }
    @Override
    public Promocion buscarPromocion(Long id) {
        return  promoRepo.findById(id).orElse(null);
    }
    @Override
    public Promocion crearPromocion(Promocion promocionACrear){
        if (promocionACrear==null){
            return null;
        }
        return  promoRepo.save(promocionACrear);
    }
    @Override
    public boolean borrarPromocion(Long id) {
        if (promoRepo.existsById(id)){
            promoRepo.deleteById(id);
            return true;
        }
        return false;
    }
    @Override
    public Promocion actualizarPromocion(Long id,Promocion promocionActualizar){
        if(promoRepo.existsById(id)){
            Promocion promoBd=buscarPromocion(id);
            promoBd.setTitulo(promocionActualizar.getTitulo());
            promoBd.setDescripcion(promocionActualizar.getDescripcion());
            promoBd.setPorcentajeDescuento(promocionActualizar.getPorcentajeDescuento());
            promoBd.setTopeReintegro(promocionActualizar.getTopeReintegro());
            promoBd.setFechaInicio(promocionActualizar.getFechaInicio());
            promoBd.setFechaFin(promocionActualizar.getFechaFin());
            promoBd.setDiasAplicacion(promocionActualizar.getDiasAplicacion());
            promoBd.setCategoria(promocionActualizar.getCategoria());
            promoBd.setBanco(promocionActualizar.getBanco());
            promoBd.setComercios(promocionActualizar.getComercios());
            return promoRepo.save(promoBd);
        }
        return null;
    }
    @Override
    public Page<Promocion> buscarPorBanco(Long bancoId, Pageable pageable) {
        return promoRepo.findByBancoId(bancoId, pageable);
    }

    @Override
    public Page<Promocion> buscarPorCategoria(Categoria categoria, Pageable pageable) {
        return promoRepo.findByCategoria(categoria, pageable);
    }

    @Override
    public Page<Promocion> buscarPorBancoYCategoria(Long bancoId, Categoria categoria, Pageable pageable) {
        return promoRepo.findByBancoIdAndCategoria(bancoId, categoria, pageable);
    }

    @Override
    @Transactional // se lo utiliza para cuando haya un error no queden datos a medio subir.
    public Promocion registrarDesdeDTO(PromocionDTO dto) {
        if (dto==null){
            return null;
        }


        Banco bancoDb=bancoService.buscarPorCod(dto.getEntidad());
        if (bancoDb==null){
            bancoDb = bancoService.buscarPorNombre(dto.getEntidad());
        }
        if (bancoDb==null){
            Banco nuevoBanco = new Banco();
            nuevoBanco.setNombre(dto.getEntidad());
            nuevoBanco.setCodigoIdentificador(dto.getEntidad().toUpperCase().replace(" ", "_"));
            nuevoBanco.setTipoEmisor(TipoEmisor.desdeTexto(dto.getEntidad()));
            nuevoBanco.setUrlLogo(obtenerLogoPorNombre(dto.getEntidad()));
            bancoDb = bancoService.crearBanco(nuevoBanco);
        } else {
            boolean modificado = false;
            if (bancoDb.getTipoEmisor() == null) {
                bancoDb.setTipoEmisor(TipoEmisor.desdeTexto(dto.getEntidad()));
                modificado = true;
            }
            if (bancoDb.getUrlLogo() == null || bancoDb.getUrlLogo().isBlank()) {
                String logo = obtenerLogoPorNombre(dto.getEntidad());
                if (logo != null) {
                    bancoDb.setUrlLogo(logo);
                    modificado = true;
                }
            }
            if (modificado) {
                bancoService.actualizarBanco(bancoDb.getId(), bancoDb);
            }
        }


        Categoria categoriaEnum = Categoria.desdeTexto(dto.getCategoria());

        List<Comercio> listaComercios = new ArrayList<>();
        if (dto.getComerciosAdheridos() != null) {
            for (String nombreComercio : dto.getComerciosAdheridos()) {
                Comercio comercio = comercioService.buscarOCrear(nombreComercio, categoriaEnum);
                if (comercio != null) {
                    listaComercios.add(comercio);
                }
            }
        }
        LocalDate inicio = null;
        LocalDate fin = null;
        if (dto.getVigencia() != null) {
            if (dto.getVigencia().getInicio() != null && !dto.getVigencia().getInicio().isBlank()) {
                inicio = LocalDate.parse(dto.getVigencia().getInicio());
            }
            if (dto.getVigencia().getFin() != null && !dto.getVigencia().getFin().isBlank()) {
                fin = LocalDate.parse(dto.getVigencia().getFin());
            }
        }

        Optional<Promocion> promoExistente = promoRepo.findByBancoAndCategoriaAndTitulo(bancoDb, categoriaEnum, dto.getTitulo());
        Promocion promoAGuardar = promoExistente.orElse(new Promocion());

        String dias = dto.getDiasAplicacion();
        if (dias == null || dias.isBlank()) {
            String textoAnalisis = (dto.getTitulo() + " " + dto.getDescripcion()).toUpperCase();
            if (textoAnalisis.contains("JUEVES")) dias = "Jueves";
            else if (textoAnalisis.contains("MIERCOLES") || textoAnalisis.contains("MIÉRCOLES")) dias = "Miércoles";
            else if (textoAnalisis.contains("LUNES")) dias = "Lunes";
            else if (textoAnalisis.contains("MARTES")) dias = "Martes";
            else if (textoAnalisis.contains("VIERNES A DOMINGO")) dias = "Viernes a Domingo";
            else if (textoAnalisis.contains("SABADO Y DOMINGO") || textoAnalisis.contains("SÁBADO Y DOMINGO") || textoAnalisis.contains("FIN DE SEMANA")) dias = "Sábado y Domingo";
            else dias = "Todos los días";
        }

        promoAGuardar.setTitulo(dto.getTitulo());
        promoAGuardar.setDescripcion(dto.getDescripcion());
        promoAGuardar.setDiasAplicacion(dias);
        promoAGuardar.setBanco(bancoDb);
        promoAGuardar.setCategoria(categoriaEnum);
        promoAGuardar.setComercios(listaComercios);
        promoAGuardar.setFechaInicio(inicio);
        promoAGuardar.setFechaFin(fin);

        return promoRepo.save(promoAGuardar);
    }

    private String obtenerLogoPorNombre(String entidad) {
        if (entidad == null) return null;
        String t = entidad.toUpperCase();
        if (t.contains("SOL")) return "/images/logos/sol.png";
        if (t.contains("UNICA") || t.contains("ÚNICA")) return "/images/logos/unica.png";
        if (t.contains("SUCREDITO") || t.contains("SUCRÉDITO")) return "/images/logos/sucredito.svg";
        if (t.contains("NARANJA")) return "/images/logos/naranjax.png";
        if (t.contains("SANTIAGO") || t.contains("BSE")) return "/images/logos/bse.png";
        if (t.contains("NACION") || t.contains("NACIÓN") || t.contains("BNA")) return "/images/logos/bna.png";
        if (t.contains("MACRO")) return "/images/logos/macro.png";
        if (t.contains("SANTANDER")) return "/images/logos/santander.png";
        if (t.contains("GALICIA")) return "/images/logos/galicia.png";
        if (t.contains("MODO")) return "/images/logos/modo.png";
        if (t.contains("MERCADO PAGO") || t.contains("MERCADOPAGO")) return "/images/logos/mercadopago.png";
        if (t.contains("VIUMI") || t.contains("VIÜMI")) return "/images/logos/viumi.png";
        return null;
    }

}
