package com.biblioteca.api.model;

import java.util.Date;
import java.util.List;

import com.biblioteca.api.DTO.book.DatosActualizarBook;
import com.biblioteca.api.DTO.book.DatosRegistroBook;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "books")
@Entity(name = "Book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "titulo")
  private String titulo;
  @Column(name = "categoria")
  private String categoria;
  @Enumerated(EnumType.STRING)
  @Column(name = "estado")
  private Estado estado; //('uploaded','review','ok')
  @Column(name = "referencia_pdf")
  private String contenidoPdf;
  @Column(name = "fecha_de_subida")
  private Date fechaSubida = new Date();

// Relación Libro - Comentarios
// Un libro puede tener muchos comentarios asociados. La relación se gestiona con una lista de objetos 'Comment'.
// El mapeo se realiza a través del campo 'book_id' en la entidad 'Comment', indicando que es la clave foránea.
  @OneToMany(mappedBy = "book_id")
  @JsonBackReference
  private List<Comment> comentarios;

// Relación Muchos a Muchos entre Libro y Autor
// Un libro puede tener múltiples autores y un autor puede estar relacionado con múltiples libros.
// Se utiliza una tabla intermedia llamada 'book_autor' para representar esta relación.
// La columna 'id_book' en la tabla intermedia referencia a la columna 'id' en la entidad 'Book'.
// La columna 'id_autor' en la tabla intermedia referencia a la columna 'id' en la entidad 'Autor'.
  @ManyToMany
  @JoinTable(
      name = "book_autores", // Nombre de la tabla intermedia
      joinColumns = @JoinColumn(name = "id_book"), // Columna en la tabla intermedia que referencia a `Book`
      inverseJoinColumns = @JoinColumn(name = "id_autor") // Columna en la tabla intermedia que referencia a `Autor`      
  )
  private List<Autor> autores;

  // Un libro puede tener múltiples palabras clave y una palabra clave puede estar asociada a múltiples libros.
  // La relación se gestiona mediante una tabla intermedia llamada 'Book_palabra_clave'.
  // La columna 'libro_id' en la tabla intermedia referencia a la columna 'id' en la entidad 'Book'.
  // La columna 'palabra_id' en la tabla intermedia referencia a la columna 'id' en la entidad 'Palabra_clave'.
  @ManyToMany
  @JoinTable(
      name = "Book_palabras_clave", // Nombre de la tabla intermedia
      joinColumns = @JoinColumn(name = "libro_id"), // Columna en la tabla intermedia que referencia a `Book`
      inverseJoinColumns = @JoinColumn(name = "palabra_id") // Columna en la tabla intermedia que referencia a `Palabra clave`
  )
  private List<Palabra_clave> palabras_clave;

  public Book(DatosRegistroBook datos) {
    this.titulo = datos.titulo();
    this.categoria = datos.categoria();
    this.estado = datos.estado();
    this.contenidoPdf = datos.referencia_pdf();
  }

  public void actualizarLibro(DatosActualizarBook datos) {
    if(datos.titulo() != null){
      this.titulo = datos.titulo();
    }
    if (datos.categoria() != null) {
      this.categoria = datos.categoria();
    }
    if (datos.estado() != null) {
      this.estado = datos.estado();
    }
    if (datos.contenidoPdf() != null) {
      this.contenidoPdf = datos.contenidoPdf();
    }
  }
}
