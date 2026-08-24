package com.promohub.backend.service;

import com.promohub.backend.model.Banco;

import java.util.List;

public interface IBancoService {

    List<Banco> traerBancos();

    Banco buscarBanco(Long id);

    Banco crearBanco(Banco bancoAGuardar);

    boolean borrarBanco(Long id);

    Banco actualizarBanco(Long id,Banco bancoActualizar);


    // Metodos Especiales para el banco
    Banco buscarPorCod(String codigoIdentificador);

    Banco buscarPorNombre(String nombre);




}
