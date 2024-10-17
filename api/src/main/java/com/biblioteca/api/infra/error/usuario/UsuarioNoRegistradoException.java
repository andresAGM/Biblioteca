package com.biblioteca.api.infra.error.usuario;

public class UsuarioNoRegistradoException extends RuntimeException {
    public UsuarioNoRegistradoException(String mensaje) {
        super(mensaje);
    }
}
