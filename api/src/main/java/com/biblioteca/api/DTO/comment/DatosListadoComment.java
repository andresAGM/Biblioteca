package com.biblioteca.api.DTO.comment;

public record DatosListadoComment(
    Integer id,
    int id_user,
    int id_book,
    String comment,
    Double reting_value
) {
}
