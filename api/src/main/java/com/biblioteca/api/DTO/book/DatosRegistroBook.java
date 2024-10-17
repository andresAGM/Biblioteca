package com.biblioteca.api.DTO.book;

import com.biblioteca.api.model.Estado;

import java.util.*;

public record DatosRegistroBook(
    String titulo, 
    String categoria, 
    Estado estado, 
    String referencia_pdf,
    List<Integer> autor_ids,
    List<Integer> ids_palabras_clave
) {}
