package com.biblioteca.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.api.model.Rol;
import com.biblioteca.api.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    // obtener usuario por email
    public Optional<User> findByEmail(String email);
    // obtener usuarios por rol
    List<User> findByRol(Rol rol);
}