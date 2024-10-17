package com.biblioteca.api.DTO.book;

import com.biblioteca.api.model.Estado;

public record DatosActualizarBook(
    Integer id,
    String titulo,
    String categoria,
    Estado estado,
    String contenidoPdf
) {}
