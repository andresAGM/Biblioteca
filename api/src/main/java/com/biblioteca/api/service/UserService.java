package com.biblioteca.api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.api.DTO.user.DatosActualizarUser;
import com.biblioteca.api.DTO.user.DatosRegistroUser;
import com.biblioteca.api.DTO.user.DatosRespuestaUser;
import com.biblioteca.api.model.Rol;
import com.biblioteca.api.model.User;
import com.biblioteca.api.repositories.RolRepository;
import com.biblioteca.api.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RolRepository rolRepository;

  // Método para registrar un nuevo usuario
  public DatosRespuestaUser registerUser(DatosRegistroUser datosRegistro) {
    // buscamos el rol por id
    Optional<Rol> rol = rolRepository.findById(datosRegistro.rolId());
    // verificar si viene vacio
    if (rol.isEmpty() || rol == null) {
      throw new IllegalArgumentException("Rol no encontrado");
    }
    // creamos el nuevo usuario con la informacion
    User user = new User();
    user.setNombre(datosRegistro.nombre());
    user.setEmail(datosRegistro.email());
    user.setPassword(datosRegistro.password());
    user.setRol(rol.get());
    // guardamos el usuario
    User savedUser = userRepository.save(user);

    // retornamos el usuario guardado
    return new DatosRespuestaUser(
            savedUser.getId(),
            savedUser.getNombre(),
            savedUser.getEmail(),
            savedUser.getRol().getId()
    );
  } 

  // Método para obtener un usuario por email
  public Optional<User> obtenerUsuarioByEmail(String email) {
    // buscamos el usuario por email
    Optional<User> user = userRepository.findByEmail(email);
    // verficar si trae informacion
    if (user.isPresent()) {
      return user;
    }
    else{
      return Optional.empty();
    }
  }

  // Método para obtener un usuario por ID
  public Optional<DatosRespuestaUser> obtenerUsuarioPorId(int id) {
    return userRepository.findById(id)
            .map(user -> new DatosRespuestaUser(user.getId(), user.getNombre(), user.getEmail(), user.getRol().getId()));
  }

  // Método para obtener todos los usuarios
  public List<DatosRespuestaUser> obtenerTodosLosUsuarios() {
    return userRepository.findAll()
            .stream()
            .map(user -> new DatosRespuestaUser(user.getId(), user.getNombre(), user.getEmail(), user.getRol().getId()))
            .toList();
  }

  // Método para eliminar un usuario por ID
  public Boolean eliminarUsuarioPorId(int id) {
    // si el usuario existe lo elimino
    if (userRepository.existsById(id)) {
        userRepository.deleteById(id);
        return true;
    } else {
         // Lanzar excepción si no se encuentra
        throw new EntityNotFoundException("El usuario con ID " + id + " no existe.");
    }
  }

  // Método para actualizar un usuario
  public User actualizarUsuario(DatosActualizarUser DatosActualizarUser) {
    // buscamos la referencia del usuario
    User user = userRepository.getReferenceById(DatosActualizarUser.id());
    // usamos el constructor actualizar
    user.actualizarUsuario(DatosActualizarUser);
    // retornamos
    return user;
  }

  // obtener usuario por id rol
  public List<DatosRespuestaUser> filtrarUsuariosPorRol(Rol rol) {
    // Obtener usuarios por rol
    List<User> usuarios = userRepository.findByRol(rol);
    
    // Convertir los usuarios a DTOs
    return usuarios.stream()
            .map(user -> new DatosRespuestaUser(user.getId(), user.getNombre(), user.getEmail(), user.getRol().getId()))
            .collect(Collectors.toList());
  }
}

