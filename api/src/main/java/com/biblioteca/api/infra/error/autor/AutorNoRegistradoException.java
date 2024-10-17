package com.biblioteca.api.infra.error.autor;

public class AutorNoRegistradoException extends RuntimeException {
    public AutorNoRegistradoException(String mensaje) {
        super(mensaje);
    }
}
