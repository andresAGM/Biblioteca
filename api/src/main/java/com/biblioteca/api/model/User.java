package com.biblioteca.api.model;

import java.util.List;

import com.biblioteca.api.DTO.user.DatosActualizarUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Entity(name = "User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name="nombre")
  private String nombre;
  @Column(name="email", unique = true)
  private String email;
  @Column(name="password")
  private String password;

  // relacion usuario - rol
  @ManyToOne // Un usuario puede tener un rol
  @JoinColumn(name = "rol_id") // columna de la db relacionada
  @JsonIgnore
  private Rol rol; // admin o user 

  // relacion usuario - comentarios
  @OneToMany(mappedBy = "user_id")
  @JsonIgnore
  private List<Comment> comentarios;

  public User(Integer id, String nombre, String email, String password, Rol rol){
    this.id = id;
    this.nombre = nombre;
    this.email = email;
    this.password = password;
    this.rol = rol;
  }

  public void actualizarUsuario(DatosActualizarUser datos){
    if (datos.nombre() != null) {
      this.nombre = datos.nombre();
    }
    if (datos.email() != null) {
      this.email = datos.email();
    }
    if (datos.password() != null) {
      this.password = datos.password();
    }
    if (datos.rolId() != null) {
      this.rol.setId(datos.rolId());
    }
  }
}
