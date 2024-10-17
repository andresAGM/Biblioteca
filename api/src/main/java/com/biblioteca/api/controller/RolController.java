package com.biblioteca.api.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.biblioteca.api.DTO.roles.DatosListadoRoles;
import com.biblioteca.api.DTO.roles.DatosRegistroRol;
import com.biblioteca.api.model.Rol;
import com.biblioteca.api.repositories.RolRepository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    @Autowired
    private RolRepository repoRol;

    @GetMapping
    public ResponseEntity<List<DatosListadoRoles>> obtenerTodosLosRoles(){
        // Obtener todos los roles desde el repositorio
        List<Rol> roles = repoRol.findAll();
        
        // Convertir la lista de Rol a DatosListadoRoles usando stream y map
        List<DatosListadoRoles> listaRolesDTO = roles.stream()
                .map(rol -> new DatosListadoRoles(rol.getId(), rol.getNombre()))
                .collect(Collectors.toList());

        // Devolver la lista en un ResponseEntity con código de estado 200 OK
        return ResponseEntity.ok(listaRolesDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<DatosListadoRoles> agregarRol(@RequestBody @Valid DatosRegistroRol datosRegistroRol) {
        // Crear una nueva instancia de Rol
        Rol nuevoRol = new Rol();
        nuevoRol.setNombre(datosRegistroRol.nombre());

        // Guardar el nuevo rol en la base de datos
        Rol rolGuardado = repoRol.save(nuevoRol);

        // Crear el DTO de respuesta
        DatosListadoRoles datosRolDTO = new DatosListadoRoles(rolGuardado.getId(), rolGuardado.getNombre());

        // Crear URI para el recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(datosRolDTO.id())
                .toUri();

        // Devolver un ResponseEntity con código 201 (Created) y la ubicación del recurso
        return ResponseEntity.created(location).body(datosRolDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable int id) {
        // Verificar si el rol existe
        if (!repoRol.existsById(id)) {
            // Si no existe, devolver un código de estado 404 (Not Found)
            return ResponseEntity.notFound().build();
        }

        // Eliminar el rol
        repoRol.deleteById(id);

        // Devolver un código de estado 204 (No Content) indicando que la eliminación fue exitosa
        return ResponseEntity.noContent().build();
    }   

}
