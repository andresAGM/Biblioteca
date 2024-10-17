package com.biblioteca.api.DTO.comment;

public record DatosRegistroComment(
    int id_user,
    int id_book,
    String comment,
    Double rating_value
) {}
