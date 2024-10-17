package com.biblioteca.api.model;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "book_autores") 
@Getter 
@NoArgsConstructor
@AllArgsConstructor
public class Book_autor {
    @Id
    @ManyToOne
    @JoinColumn(name = "id_book")
    private Book id_book;    

    @Id
    @ManyToOne
    @JoinColumn(name = "id_autor")
    private Autor id_autor;
}
