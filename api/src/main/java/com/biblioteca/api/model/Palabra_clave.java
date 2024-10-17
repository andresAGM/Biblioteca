package com.biblioteca.api.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "palabras_clave") 
@Entity(name = "Palabra_clave")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Palabra_clave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String palabra; 

    // relacion con book
    @ManyToMany(mappedBy = "palabras_clave")
    private Set<Book> books = new HashSet<>();
}
