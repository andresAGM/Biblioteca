package com.biblioteca.api.DTO.comment;

import java.util.Date;

public record DatosListadoCommentByIdBook(
    int id,
    String comment,
    Double rating_value,
    Date created_at,
    int id_usuario,
    String nombre_usuario,
    String titulo_libro
){}
