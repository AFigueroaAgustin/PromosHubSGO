package com.promohub.backend.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String recurso,Long id){
        super(recurso + " con el ID "+ id + " no encontrado");
    }
}
