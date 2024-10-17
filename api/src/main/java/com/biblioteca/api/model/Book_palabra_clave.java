package com.biblioteca.api.model;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "book_palabra_clave") 
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book_palabra_clave {
    @Id
    @ManyToOne
    @JoinColumn(name = "libro_id")
    private Book libro_id;    
    @Id
    @ManyToOne
    @JoinColumn(name = "palabra_id")
    private Palabra_clave palabra_id;  
}