package com.biblioteca.api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.biblioteca.api.DTO.book.DatosActualizarBook;
import com.biblioteca.api.DTO.book.DatosListadoBook;
import com.biblioteca.api.DTO.book.DatosRegistroBook;
import com.biblioteca.api.DTO.book.DatosRespuestaBook;
import com.biblioteca.api.infra.error.autor.AutorNoRegistradoException;
import com.biblioteca.api.infra.error.palabraClave.PalabraClaveNoRegistradaException;
import com.biblioteca.api.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import com.biblioteca.api.model.Book;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/books")
public class BookController {
    // inyeccion del servicio
    @Autowired
    private BookService bookService;
    
    @GetMapping()
    @Operation(summary = "Obtener todos los libros de la base de datos.")
    public ResponseEntity<List<DatosListadoBook>> obtenerLibros() {
        Optional<List<DatosListadoBook>> libros = bookService.obtenerTodosLosLibros();
        return libros.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}") // obtener libro por id
    @Operation(summary = "Obtener libro por id.")
    public ResponseEntity<DatosListadoBook> obtenerLibroPorId(@PathVariable Integer id){
        Optional<Book> libro = bookService.obtenerLibroPorId(id);
        // Verificar si el libro está presente
        if (libro.isPresent()) {
            // conversion de Optional a DatosListadoBook
            DatosListadoBook datoLibro = new DatosListadoBook(libro.get());
            return ResponseEntity.ok(datoLibro);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping() // registrar libro
    @Operation(summary = "Registrar un libro.")
    public ResponseEntity<DatosRespuestaBook> guardarLibro(@RequestBody DatosRegistroBook datosRegistro) {
       try {
            // Llamar al servicio para agregar el libro
            DatosRespuestaBook libroGuardado = bookService.agregarLibro(datosRegistro);

            // Crear URI para el recurso recién creado
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(libroGuardado.id())
                .toUri();

            // Devolver un ResponseEntity con código 201 y la ubicación del recurso
            return ResponseEntity.created(location).body(libroGuardado);
        }catch(AutorNoRegistradoException i){
            System.out.println("Error: " + i.getMessage());
            return ResponseEntity.notFound().build();
        }catch(PalabraClaveNoRegistradaException a){
            System.out.println("Error: " + a.getMessage());
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            System.out.println("Ocurrio un error: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un libro.")
    @Transactional // eliminar un libro
    public ResponseEntity<Void> eliminarLibro(@PathVariable Integer id){
        try {
            bookService.eliminarLibro(id);
            return ResponseEntity.noContent().build(); // Código 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Código 404 Not Found
        } catch (Exception e) {
            // Código 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping // actualizar
    @Transactional // envia los cambios a la base de datos
    @Operation(summary = "Actualizar un libro.")
    public ResponseEntity<DatosListadoBook> actualizarLibro(@RequestBody DatosActualizarBook datosActualizar){
        try{
            // llamo el metodo actualizar del servicio
            Book libro = bookService.actualizarLibro(datosActualizar);
            // verifico si viene vacio
            if (libro == null) {
                ResponseEntity.notFound().build(); // 404
            }
            // envio status 200 con el libro actualizado
            DatosListadoBook datosListado = new DatosListadoBook(libro);
            return ResponseEntity.ok(datosListado); // status 200
        }catch(Exception e){
            System.out.println("Error al actualizar: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // filtros
    @GetMapping("/search/{titulo}") // buscar libro por titulo
    @Operation(summary = "Buscar libro por titulo, prefijo, una o mas palabras.")
    public ResponseEntity<List<DatosListadoBook>> buscarLibroPorTitulo(@PathVariable String titulo){
        try{
            // aplicamos el comodin a texto pequeños o prefijos
            if (titulo.length() >= 0 && titulo.length() < 11) {
                titulo = titulo + "*";
            }
            List<DatosListadoBook> libro = bookService.buscarLibroPorTitulo(titulo);
            // verificamos si viene vacio
            if (libro == null || libro.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(libro); // 200
        }catch(Exception e){
            System.out.println("Error al buscar libro por titulo: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/categoria/{nombre}") // buscar libro por categoria
    @Operation(summary = "Buscar libro por categoria.")
    public ResponseEntity<List<DatosListadoBook>> buscarLibroPorCategoria(@PathVariable String nombre) {
        //return bookService.buscarLibroPorCategoria(nombre);
        try{
            List<DatosListadoBook> libro = bookService.buscarLibroPorCategoria(nombre);
            // verificamos si viene vacio
            if (libro == null || libro.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(libro); // 200
        }catch(Exception e){
            System.out.println("Error al buscar libro por titulo: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }    
}
