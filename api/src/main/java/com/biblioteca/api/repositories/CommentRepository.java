package com.biblioteca.api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.biblioteca.api.DTO.comment.DatosListadoCommentByIdBook;
import com.biblioteca.api.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    // filtrar comentarios por id del libro
    @Query("SELECT new com.biblioteca.api.DTO.comment.DatosListadoCommentByIdBook(c.id, c.comment, c.rating_value, c.created_at, u.id, u.nombre, b.titulo) FROM Comment c JOIN User u ON c.user_id.id = u.id JOIN Book b ON c.book_id.id = b.id WHERE b.id = :id_libro")
    public List<DatosListadoCommentByIdBook> obtenerComentariosPorIdBook(int id_libro); 

    // obtener todos los comentarios en forma de pageable, de 5 unidades
    @Query("SELECT c FROM Comment c")
    public Page<Comment> obtenerTodosLosComentarios(Pageable pageable);

    // obtener comentarios por rating de mayor a menor
    @Query("SELECT c FROM Comment c ORDER BY c.rating_value DESC LIMIT 10")
    public List<Comment> findTop10ByCommentOrderByRating();
}
