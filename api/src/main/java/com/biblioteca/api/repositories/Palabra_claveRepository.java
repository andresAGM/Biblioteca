package com.biblioteca.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.api.model.Palabra_clave;

public interface Palabra_claveRepository extends JpaRepository<Palabra_clave, Integer> {
    // registrar palabra clave
    // obtener todas las palabras clave
    // obtener las palabras clave de un libro
    // eliminar palabra clave
    // actualizar palabra clave
}
