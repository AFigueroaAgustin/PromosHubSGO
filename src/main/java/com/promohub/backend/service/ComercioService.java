package com.promohub.backend.service;

import com.promohub.backend.model.Categoria;
import com.promohub.backend.model.Comercio;
import com.promohub.backend.repository.IComercioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComercioService implements IComercioService {

    private final IComercioRepository comerciorepo;

    public ComercioService(IComercioRepository comerciorepo) {
        this.comerciorepo = comerciorepo;
    }

    @Override
    public List<Comercio> traerComercios() {
        return comerciorepo.findAll();
    }

    @Override
    public Comercio buscarComercio(Long id) {
        return comerciorepo.findById(id).orElse(null);
    }

    @Override
    public Comercio crearComercio(Comercio comercioACrear) {
        if (comercioACrear == null) {
            return null;
        }
        return comerciorepo.save(comercioACrear);
    }

    @Override
    public boolean borrarComercio(Long id) {
        if (comerciorepo.existsById(id)) {
            comerciorepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Comercio actualizarComercio(Long id, Comercio comercioActualizar) {

        if (comerciorepo.existsById(id)) {
            Comercio comercioBd = buscarComercio(id);
            comercioBd.setNombre(comercioActualizar.getNombre());
            comercioBd.setCategoria(comercioActualizar.getCategoria());
            comercioBd.setDireccion(comercioActualizar.getDireccion());
            comercioBd.setCiudad(comercioActualizar.getCiudad());
            comercioBd.setLatitud(comercioActualizar.getLatitud());
            comercioBd.setLongitud(comercioActualizar.getLongitud());
            return comerciorepo.save(comercioBd);
        }
        return null;
    }

    @Override
    public Comercio buscarComercioPorNombre(String nombre) {
        return comerciorepo.findByNombreIgnoreCase(nombre).orElse(null);
    }

    public Comercio buscarOCrear(String nombre, Categoria categoria){
        if(nombre == null || nombre.isBlank()){
            return null;
        }

        Comercio comercioExiste=buscarComercioPorNombre(nombre.strip());
        if(comercioExiste!=null){
            return comercioExiste;
        }

        Comercio nuevoComercio = new Comercio();
        nuevoComercio.setNombre(nombre.trim());
        nuevoComercio.setCategoria(categoria != null ? categoria : Categoria.VARIOS);
        //Lo persistimos en la base de datos y lo retornamos
        return comerciorepo.save(nuevoComercio);
    }


}
