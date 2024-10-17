package com.biblioteca.api.DTO.book;

import com.biblioteca.api.model.Book;

public record DatosListadoBook(Integer id, String titulo, String categoria) {
    public DatosListadoBook(Book libro) {
        this(libro.getId(), libro.getTitulo(), libro.getCategoria());
    }
}
