package com.biblioteca.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.api.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer>{
    
}
