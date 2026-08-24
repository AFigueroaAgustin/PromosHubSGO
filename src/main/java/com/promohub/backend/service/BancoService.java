package com.promohub.backend.service;

import com.promohub.backend.model.Banco;
import com.promohub.backend.repository.IBancoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BancoService implements IBancoService{

    private final IBancoRepository bancorepo;
     // Inyeccion por constructor
    public BancoService(IBancoRepository bancorepo) {
        this.bancorepo = bancorepo;
    }

    @Override
    public List<Banco> traerBancos() {
        return bancorepo.findAll();
    }

    @Override
    public Banco buscarBanco(Long id) {
        return bancorepo.findById(id).orElse(null);
    }

    @Override
    public Banco crearBanco(Banco bancoAGuardar) {
        if (bancoAGuardar == null) {
            return null;
        }
        return bancorepo.save(bancoAGuardar); // se devuelve asi para obtener el banco completo no el banco logico
    }

    @Override
    public boolean borrarBanco(Long id) {
        if (bancorepo.existsById(id)) {
            bancorepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Banco actualizarBanco(Long id,Banco bancoActualizar) {
        if (bancorepo.existsById(id)) {
            Banco bancoBd=buscarBanco(id);
            bancoBd.setNombre(bancoActualizar.getNombre());
            bancoBd.setCodigoIdentificador(bancoActualizar.getCodigoIdentificador());
            bancoBd.setSitioWeb(bancoActualizar.getSitioWeb());
            bancoBd.setUrlLogo(bancoActualizar.getUrlLogo());
            if (bancoActualizar.getTipoEmisor() != null) {
                bancoBd.setTipoEmisor(bancoActualizar.getTipoEmisor());
            }
            return bancorepo.save(bancoBd);
        }

        return null;
    }

    @Override
    public Banco buscarPorCod(String codigoIdentificador) {
        return bancorepo.findByCodigoIdentificador(codigoIdentificador).orElse(null);
    }

    @Override
    public Banco buscarPorNombre(String nombre) {

        return bancorepo.findByNombre(nombre).orElse(null);
    }
}
