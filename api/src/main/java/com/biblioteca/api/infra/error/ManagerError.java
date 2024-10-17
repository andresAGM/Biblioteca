package com.biblioteca.api.infra.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.biblioteca.api.infra.error.autor.AutorNoRegistradoException;
import com.biblioteca.api.infra.error.libro.LibroNoRegistradoException;
import com.biblioteca.api.infra.error.palabraClave.PalabraClaveNoRegistradaException;
import com.biblioteca.api.infra.error.usuario.UsuarioNoRegistradoException;

@RestControllerAdvice
public class ManagerError {
    @ExceptionHandler(AutorNoRegistradoException.class)
    public ResponseEntity<String> handleAutorNoRegistrado(AutorNoRegistradoException ex) {
        return new ResponseEntity<>("{\"error\":\"Autor no registrado\"}", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PalabraClaveNoRegistradaException.class)
    public ResponseEntity<String> handlePalabraClaveNoRegistrada(PalabraClaveNoRegistradaException ex){
        return new ResponseEntity<>("{\"error\":\"Palabra clave no registrada\"}", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsuarioNoRegistradoException.class)
    public ResponseEntity<String> handleUsuarioNoRegistrado(UsuarioNoRegistradoException ex){
        return new ResponseEntity<>("{\"error\":\"Usuario no registrado\"}", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LibroNoRegistradoException.class)
    public ResponseEntity<String> handleLibroNoRegistrado(LibroNoRegistradoException ex){
        return new ResponseEntity<>("{\"error\":\"Libro no registrado\"}", HttpStatus.BAD_REQUEST);
    }
}
