package com.promohub.backend.service;

import com.promohub.backend.model.Categoria;
import com.promohub.backend.model.Comercio;

import java.util.List;

public interface IComercioService {

    List<Comercio> traerComercios();

    Comercio buscarComercio(Long id);

    Comercio crearComercio(Comercio comercioACrear);

    boolean borrarComercio(Long id);

    Comercio actualizarComercio(Long id,Comercio comercioActualizar);

    // Metodo especiales

    Comercio buscarComercioPorNombre(String nombre);

    Comercio buscarOCrear(String nombre, Categoria categoria);

}
