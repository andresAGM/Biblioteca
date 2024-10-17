package com.biblioteca.api.DTO.user;

public record DatosRespuestaUser(
    Integer id,
    String nombre,
    String email,
    Integer rolId
) {}
