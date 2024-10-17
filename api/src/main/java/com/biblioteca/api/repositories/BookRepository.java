package com.biblioteca.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import com.biblioteca.api.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
     // buscar libro por titulo
    @Query(value = "SELECT * FROM books WHERE MATCH(titulo) AGAINST(:titulo IN BOOLEAN MODE)", nativeQuery = true)
    public List<Book> buscarLibroPorTitulo(String titulo);

    // buscar libro por categoria
    @Query("SELECT b FROM Book b WHERE categoria LIKE %:categoria%")
    public List<Book> buscarLibroPorCategoria(String categoria);
}