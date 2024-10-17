package com.biblioteca.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.biblioteca.api.DTO.comment.DatosActualizarComentario;
import com.biblioteca.api.DTO.comment.DatosListadoComment;
import com.biblioteca.api.DTO.comment.DatosListadoCommentByIdBook;
import com.biblioteca.api.DTO.comment.DatosRegistroComment;
import com.biblioteca.api.infra.error.libro.LibroNoRegistradoException;
import com.biblioteca.api.infra.error.usuario.UsuarioNoRegistradoException;
import com.biblioteca.api.model.Comment;
import com.biblioteca.api.service.CommentService;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/books/comments")
public class CommentController {
    @Autowired // servicio
    public CommentService commentService;

    @GetMapping()
    public ResponseEntity<List<DatosListadoComment>> obtenerTodosLosComentarios(@NonNull Pageable pageable) {
        try{
            Optional<List<DatosListadoComment>> comentarios = commentService.obtenerTodosLosComentarios(pageable);
            // si viene vacio o null
            if (comentarios == null || comentarios.isEmpty()) {
                return ResponseEntity.notFound().build(); // 404 Not Found
            } 
            // 200 o 404
            return comentarios.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }catch(Exception e){
            System.out.println("Ocurrio un error al mostrar todos los comentarios: " + e.getMessage());
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @PostMapping() // registrar comentario
    public ResponseEntity<DatosListadoComment> registrarComentario(@RequestBody DatosRegistroComment datosRegistroComment) {
        try{
            DatosListadoComment comentario = commentService.crearCommentario(datosRegistroComment);
            
            // Crear URI para el recurso recién creado
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(comentario.id())
                .toUri();

            // Devolver un ResponseEntity con código 201 y la ubicación del recurso
            return ResponseEntity.created(location).body(comentario);
        }catch(UsuarioNoRegistradoException i){
            System.out.println("No se encontro el recurso: " + i.getMessage());
            return ResponseEntity.notFound().build();
        }catch(LibroNoRegistradoException i){
            System.out.println("No se encontro el recurso: " + i.getMessage());
            return ResponseEntity.notFound().build();
        }catch(Exception e){
            System.out.println("Ocurrio un error: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id_libro}") // obtener comentarios por id del libro
    public ResponseEntity<List<DatosListadoCommentByIdBook>> obtenerComentariosPorIdBook(@PathVariable int id_libro) {
        try{
            // hacemos la consulta
            Optional<List<DatosListadoCommentByIdBook>> comentarios = commentService.obtenerComentariosPorIdLibro(id_libro);
            // verificamos si retorna informacion
            if (comentarios.isEmpty()) {
                return ResponseEntity.notFound().build(); // 404
            }
            // retornamos los comentarios
            return ResponseEntity.ok(comentarios.get());
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/top10/rating")
    public ResponseEntity<List<DatosListadoComment>> obtenerTop10CommentsByRating() {
        return ResponseEntity.ok(commentService.obtenerComentariosPorRatingDesc());
    }

    @DeleteMapping("/delete/{id}")
    @Transactional // eliminar un comentario
    public ResponseEntity<Void> eliminarComentario(@PathVariable Integer id){
        try {
            commentService.eliminarComentario(id);
            return ResponseEntity.noContent().build(); // Código 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Código 404 Not Found
        } catch (Exception e) {
            // Código 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update") // actualizar
    @Transactional // envia los cambios a la base de datos
    public ResponseEntity<DatosListadoComment> actualizarComentario(@RequestBody DatosActualizarComentario datosActualizar){
        try{
            // llamo el metodo actualizar del servicio
            Comment comentario = commentService.actualizarComentario(datosActualizar);
            // verifico si viene vacio
            if (comentario == null) {
                ResponseEntity.notFound().build(); // 404
            }
            // envio status 200 con el comentario actualizado
            @SuppressWarnings("null")
            DatosListadoComment datosListado = new DatosListadoComment(comentario.getId(), comentario.getUser_id().getId(), comentario.getBook_id().getId(), comentario.getComment(), comentario.getRating_value());
            return ResponseEntity.ok(datosListado); // status 200
        }catch(Exception e){
            System.out.println("Error al actualizar: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
