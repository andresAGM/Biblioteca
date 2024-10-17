package com.biblioteca.api.DTO.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroUser(
    @NotBlank String nombre,
    @NotBlank String email,
    @NotBlank String password,
    @NotNull Integer rolId
) {}
