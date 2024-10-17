package com.biblioteca.api.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.biblioteca.api.DTO.comment.DatosActualizarComentario;
import com.biblioteca.api.DTO.comment.DatosListadoComment;
import com.biblioteca.api.DTO.comment.DatosListadoCommentByIdBook;
import com.biblioteca.api.DTO.comment.DatosRegistroComment;
import com.biblioteca.api.infra.error.libro.LibroNoRegistradoException;
import com.biblioteca.api.infra.error.usuario.UsuarioNoRegistradoException;
import com.biblioteca.api.model.Book;
import com.biblioteca.api.model.Comment;
import com.biblioteca.api.model.User;
import com.biblioteca.api.repositories.BookRepository;
import com.biblioteca.api.repositories.CommentRepository;
import com.biblioteca.api.repositories.UserRepository;

@Service
public class CommentService {
    // repositorio comment
    @Autowired
    public CommentRepository repoComment;

    // repositorio user
    @Autowired
    public UserRepository repoUser;

    // repositorio user
    @Autowired
    public BookRepository repoBook;

    public DatosListadoComment crearCommentario(DatosRegistroComment datosRegistro){
        // buscamos el usuario
        Optional<User> user = repoUser.findById(datosRegistro.id_user());
        // 404
        if (user == null || user.isEmpty()) {
            throw new UsuarioNoRegistradoException("Usuario no encontrado");
        }
        // buscamos el libro
        Optional<Book> book = repoBook.findById(datosRegistro.id_book());
        // 404
        if (book == null || book.isEmpty()) {
            throw new LibroNoRegistradoException("Libro no encontrado");
        }
        // creamos el nuevo comentario
        Comment comentarioNuevo = new Comment();
        comentarioNuevo.setUser_id(user.get());
        comentarioNuevo.setBook_id(book.get());
        comentarioNuevo.setComment(datosRegistro.comment());
        comentarioNuevo.setRating_value(datosRegistro.rating_value());
        comentarioNuevo.setCreated_at(new Date());

        // guardamos
        repoComment.save(comentarioNuevo);
        
        //convertir el nuevo comentario a DatosListadoComment
        DatosListadoComment datosListado = new DatosListadoComment(comentarioNuevo.getId(), comentarioNuevo.getUser_id().getId(), comentarioNuevo.getBook_id().getId(), comentarioNuevo.getComment(), comentarioNuevo.getRating_value());

        return datosListado;
    }

    public Optional<List<DatosListadoComment>> obtenerTodosLosComentarios(Pageable pageable){
        Page<Comment> comentarios = repoComment.obtenerTodosLosComentarios(pageable); // all comments
        
        // convertir de List<Comment> a List<DatosListadoComment>        
        List<DatosListadoComment> listadoComments = comentarios.getContent().stream()
            .map(comment -> new DatosListadoComment(comment.getId(), comment.getUser_id().getId(), comment.getBook_id().getId(),comment.getComment(), comment.getRating_value()))
            .collect(Collectors.toList());
        // retornamos la lista convertida
        return listadoComments.isEmpty() ? Optional.empty() : Optional.of(listadoComments);
    }

    // filtrar comentarios por id del libro
    public Optional<List<DatosListadoCommentByIdBook>> obtenerComentariosPorIdLibro(int id_libro){
        // consultamos los comentarios
        List<DatosListadoCommentByIdBook> comentarios = repoComment.obtenerComentariosPorIdBook(id_libro);
        // retornamos los comentarios
        return comentarios.isEmpty() ? Optional.empty() : Optional.of(comentarios);
    }

    // obtener comentarios por rating de mayor rating a menor
    public List<DatosListadoComment> obtenerComentariosPorRatingDesc(){
        // realizamos la consulta
        List<Comment> comments = repoComment.findTop10ByCommentOrderByRating();
        // hacemos la convercion usando stream
        List<DatosListadoComment> datosListado = comments.stream()
        .map(comment -> new DatosListadoComment(comment.getId(), comment.getUser_id().getId(), comment.getBook_id().getId(), comment.getComment(), comment.getRating_value()))
        .collect(Collectors.toList());
        // retornamos la lista
        return datosListado;
    }

    // eliminar comentario
    public void eliminarComentario(Integer id) {
        if (repoComment.existsById(id)) {
            repoComment.deleteById(id);
        } else {
            throw new IllegalArgumentException("Comentario con ID " + id + " no existe.");
        }
    }

    // actualizar comentarios
    public Comment actualizarComentario(DatosActualizarComentario datos){
        // obtenemos la referencia del comentario
        Comment comentario = repoComment.getReferenceById(datos.id());
        comentario.actualizarComentario(datos);
        return comentario;
    }
}
