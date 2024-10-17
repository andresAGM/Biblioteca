package com.biblioteca.api.model;

import java.util.Date;

import com.biblioteca.api.DTO.comment.DatosActualizarComentario;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "comments")
@Entity(name = "Comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // relacion con usuario
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user_id;

    // relacion con libros
    @ManyToOne
    @JoinColumn(name = "book_id") // llave foranea de la base de datos
    private Book book_id;

    private String comment;
    private Double rating_value;
    private Date created_at;

    public Comment(Integer id, User user_id, Book book_id, String comment, Double rating_value){
        this.id = id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.comment = comment;
        this.rating_value = rating_value;
        this.created_at = new Date();
    }

     public void actualizarComentario(DatosActualizarComentario datos) {
        if(datos.comment() != null){
        this.comment = datos.comment();
        }
        if (datos.rating_value() != null) {
        this.rating_value = datos.rating_value();
        }
    }
}
