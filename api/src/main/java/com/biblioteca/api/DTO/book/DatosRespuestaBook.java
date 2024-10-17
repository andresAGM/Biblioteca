package com.biblioteca.api.DTO.book;

import com.biblioteca.api.model.Estado;
import java.util.Date;

public record DatosRespuestaBook(
    Integer id,
    String titulo,
    String categoria,
    Estado estado,
    String referencia_pdf,
    Date fecha_de_subida
){}
