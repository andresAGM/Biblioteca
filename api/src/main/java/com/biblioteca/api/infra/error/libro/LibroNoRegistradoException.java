package com.biblioteca.api.infra.error.libro;

public class LibroNoRegistradoException extends RuntimeException {
    public LibroNoRegistradoException(String mensaje) {
        super(mensaje);
    }
}
