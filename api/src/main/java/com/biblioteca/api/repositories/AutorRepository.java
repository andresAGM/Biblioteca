package com.biblioteca.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.api.model.Autor;

public interface AutorRepository extends JpaRepository<Autor, Integer> {
    // registrar autor
    // obtener autor por id
    // obtener autor por nombre
    // obtener los autores de un libro
    // actualizar un autor
    // eliminar un autor
}
