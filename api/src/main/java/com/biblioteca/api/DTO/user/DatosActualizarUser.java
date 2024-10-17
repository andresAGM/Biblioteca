package com.biblioteca.api.DTO.user;

public record DatosActualizarUser(
    int id,
    String nombre,
    String email,
    String password,
    Integer rolId
) {}
