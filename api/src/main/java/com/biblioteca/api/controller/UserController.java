package com.biblioteca.api.controller;

import java.util.Optional;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

//import com.biblioteca.api.DTO.EjemploNombre;

import com.biblioteca.api.DTO.user.DatosActualizarUser;
import com.biblioteca.api.DTO.user.DatosListadoUser;
import com.biblioteca.api.DTO.user.DatosRegistroUser;
import com.biblioteca.api.DTO.user.DatosRespuestaUser;
import com.biblioteca.api.model.Rol;
import com.biblioteca.api.model.User;
import com.biblioteca.api.repositories.RolRepository;
import com.biblioteca.api.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RolRepository rolRepository;

    // Endpoint para registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<DatosRespuestaUser> registrarUsuario(@RequestBody DatosRegistroUser datosRegistro) {
        try {
            DatosRespuestaUser nuevoUsuario = userService.registerUser(datosRegistro);
            
            // Crear URI para el recurso recién creado
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoUsuario.id())
                .toUri();

            // Devolver un ResponseEntity con código 201 y la ubicación del recurso
            return ResponseEntity.created(location).body(nuevoUsuario);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para obtener un usuario por email
    @GetMapping("/email/{email}")
    public ResponseEntity<DatosRespuestaUser> obtenerUsuarioPorEmail(@PathVariable String email) {
        Optional<User> user = userService.obtenerUsuarioByEmail(email);
        if (user.isPresent()) {
            DatosRespuestaUser datosRespuesta = new DatosRespuestaUser(
                    user.get().getId(),
                    user.get().getNombre(),
                    user.get().getEmail(),
                    user.get().getRol().getId()
            );
            return ResponseEntity.ok(datosRespuesta);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Endpoint para obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaUser> obtenerUsuarioPorId(@PathVariable int id) {
        Optional<DatosRespuestaUser> usuario = userService.obtenerUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // Endpoint para obtener todos los usuarios
    @GetMapping()
    public ResponseEntity<List<DatosRespuestaUser>> obtenerTodosLosUsuarios() {
        List<DatosRespuestaUser> usuarios = userService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint para eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuarioPorId(@PathVariable int id) {
        try {
            boolean eliminado = userService.eliminarUsuarioPorId(id);
            if (eliminado) {
                return ResponseEntity.ok("Usuario eliminado correctamente.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para actualizar un usuario
    @PutMapping("/update")
    @Transactional // envia la informacion a la base de datos
    public ResponseEntity<DatosListadoUser> actualizarUsuario(@RequestBody DatosActualizarUser datosActualizarUser) {
        try {
            User usuarioActualizado = userService.actualizarUsuario(datosActualizarUser);
            // convertir a DatosListadoUser
            DatosListadoUser datosListado = new DatosListadoUser(usuarioActualizado.getId(), usuarioActualizado.getNombre(), usuarioActualizado.getEmail(), usuarioActualizado.getRol().getId());   
            // retornamos los datos del usuario
            return ResponseEntity.ok(datosListado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // EndPoint para buscar usuarios por roles
    @GetMapping("rol/{rolId}")
    public ResponseEntity<List<DatosRespuestaUser>> obtenerUsuariosPorRol(@PathVariable int rolId) {
        // Buscar rol por ID
        Optional<Rol> rolOptional = rolRepository.findById(rolId);
        
        if (rolOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // Rol no encontrado
        }
        
        List<DatosRespuestaUser> usuariosPorRol = userService.filtrarUsuariosPorRol(rolOptional.get());
        return ResponseEntity.ok(usuariosPorRol);
    }
}
