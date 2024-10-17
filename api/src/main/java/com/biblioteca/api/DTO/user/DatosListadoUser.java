package com.biblioteca.api.DTO.user;

public record DatosListadoUser(
    int id,
    String nombre,
    String email,
    int rolId
) {}
