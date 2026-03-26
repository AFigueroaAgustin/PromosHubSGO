package com.promohub.backend.exception;

public class DuplicateResourceException extends RuntimeException{
    public DuplicateResourceException(String recurso,String titulo){
        super(recurso+" con el titulo "+ titulo + " ya existe en la base de datos.");
    }
}
