package com.biblioteca.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.api.DTO.book.DatosActualizarBook;
import com.biblioteca.api.DTO.book.DatosListadoBook;
import com.biblioteca.api.DTO.book.DatosRegistroBook;
import com.biblioteca.api.DTO.book.DatosRespuestaBook;
import com.biblioteca.api.infra.error.autor.AutorNoRegistradoException;
import com.biblioteca.api.infra.error.palabraClave.PalabraClaveNoRegistradaException;
import com.biblioteca.api.model.Book;
import com.biblioteca.api.model.Palabra_clave;
import com.biblioteca.api.model.Autor;
import com.biblioteca.api.repositories.BookRepository;
import com.biblioteca.api.repositories.Palabra_claveRepository;
import com.biblioteca.api.repositories.AutorRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BookService {
  /*
  En esta clase se encargara de la logica de negocio
  como guardar libros y obtenerlos
   */

  // inyectar el repositorio para el manejo de la tabla books
  @Autowired
  private BookRepository repoBook;

  // inyectar el repositorio para el manejo de la tabla autores
  @Autowired
  private AutorRepository repoAutores;

  // inyectar el repositorio para el manejo de la tabla palabras_clave
  @Autowired
  private Palabra_claveRepository repoPalabrasClave;

  private List<Book> libros = new ArrayList<>();

  public DatosRespuestaBook agregarLibro(DatosRegistroBook datosRegistro) {
    // bucamos los autores en la base de datos
    List<Autor> autores = repoAutores.findAllById(datosRegistro.autor_ids());
    // verificamos si encontro los autores
    if (autores.isEmpty()) {
      throw new AutorNoRegistradoException("No se encontro uno o mas autores.");
    }
    // buscamos las palabras clave
    List<Palabra_clave> palabras_claves = repoPalabrasClave.findAllById(datosRegistro.ids_palabras_clave());
    // verificamos si encontro las palabras clave
    if (palabras_claves.isEmpty() || palabras_claves.size() != datosRegistro.ids_palabras_clave().size()) {
      throw new PalabraClaveNoRegistradaException("No se encontro una o mas palabras clave.");
    }
    // asignamos los autores al libro
    Book nuevoLibro = new Book();
    nuevoLibro.setTitulo(datosRegistro.titulo());
    nuevoLibro.setCategoria(datosRegistro.categoria());
    nuevoLibro.setEstado(datosRegistro.estado());
    nuevoLibro.setContenidoPdf(datosRegistro.referencia_pdf());
    nuevoLibro.setAutores(autores);
    nuevoLibro.setPalabras_clave(palabras_claves);
    // guardamos el libro en la db
    repoBook.save(nuevoLibro); 
    // hacemos la conversion
    DatosRespuestaBook datosRespuesta = new DatosRespuestaBook(nuevoLibro.getId(), nuevoLibro.getTitulo(), nuevoLibro.getCategoria(), nuevoLibro.getEstado(), nuevoLibro.getContenidoPdf(), nuevoLibro.getFechaSubida());
    // retornamos la conversion
    return datosRespuesta;
  }

  // Agregar pageable para mostrar de a 10 elementos
  public Optional<List<DatosListadoBook>> obtenerTodosLosLibros() {
    libros = repoBook.findAll(); // Obtiene todos los libros de la base de datos
    // Convierte List<Book> a List<DatosListadoBook>
    List<DatosListadoBook> listadoBooks = libros.stream()
            .map(book -> new DatosListadoBook(book.getId(), book.getTitulo(), book.getCategoria()))
            .collect(Collectors.toList());
    // lista convertida y retornar Optional
    return listadoBooks.isEmpty() ? Optional.empty() : Optional.of(listadoBooks);
  }

  public Optional<Book> obtenerLibroPorId(int id) {
    Optional<Book> libro = repoBook.findById(id);
    if(libro.isPresent()){
      return libro; // libro encontrado
    }else{
      System.out.println("Libro no encontrado.");
      return Optional.empty(); // libro no encontrado
    }
  }

  public void eliminarLibro(int id) {
    try {
      // Verificar si el libro existe antes de eliminarlo
      if (!repoBook.existsById(id)) {
        // Lanzar excepción si no se encuentra
        throw new EntityNotFoundException("El libro con ID " + id + " no existe.");
      }
      // eliminamos el libro
      repoBook.deleteById(id);
    } catch (EntityNotFoundException e) {
        // Log específico para no encontrado
        System.err.println(e.getMessage());
        throw e; // Re-lanzar la excepción para que el controlador la maneje
    } catch (Exception e) {
        // Manejar otras excepciones
        System.err.println("Error al eliminar el libro: " + e.getMessage());
        // Lanzar RuntimeException o una excepción personalizada
        throw new RuntimeException("Error al eliminar el libro", e); 
    }
  }

  public Book actualizarLibro(DatosActualizarBook datos){
    // obtenemos la referencia del libro
    Book libro = repoBook.getReferenceById(datos.id());
    libro.actualizarLibro(datos);
    return libro;
  }

  // filtros
  public List<DatosListadoBook> buscarLibroPorTitulo(String titulo){
    List<Book> libros = repoBook.buscarLibroPorTitulo(titulo);
    List<DatosListadoBook> datosListado = libros.stream()
    .map(book -> new DatosListadoBook(book.getId(), book.getTitulo(), book.getCategoria())) // Conversión a DTO
    .collect(Collectors.toList()); // Recopilar resultados en una lista
    return datosListado;
  }

  public List<DatosListadoBook> buscarLibroPorCategoria(String categoria){
    List<Book> libros = repoBook.buscarLibroPorCategoria(categoria);
    List<DatosListadoBook> listaConvertida = libros.stream()
    .map(book -> new DatosListadoBook(book.getId(), book.getTitulo(), book.getCategoria()))
    .collect(Collectors.toList());
    return listaConvertida;
  }
}


