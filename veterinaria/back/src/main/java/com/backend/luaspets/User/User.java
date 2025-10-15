package com.backend.luaspets.User;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.backend.luaspets.persistance.entity.Cart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

//Anotaciones JPA para trabajar con ORM (mapeo objeto-relacional) y convertir esta clase en una entidad de BDD.
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

//Librería que genera automaticamente getters y setters, constructores (simples y param.)
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Genera getters y setters
@Builder //Permite construir el objeto de forma fluida
@NoArgsConstructor //constructor vacío
@AllArgsConstructor //constructor parametrizado
@Entity //Indica una entidad JPA y se mapeará a una tabla en la BD

//Define nombre de la tabla y establece username con valores únicos
@Table(name="user", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})


//Implementar UserDetails para que SpringSecurity reconozca los usuarios, roles y mas detalles de seguridad para control de accesos en el aplicativo.
//UserDetails define metodos como: GetAutorithies, isAccountNonExpired, getUsername, getPassword, etc.
public class User implements UserDetails {

    @Id //establece primary key
    @GeneratedValue //se genera auto. Auto Incrementable
    Integer id;

    @Basic //Se definen las caracteristicas de los campos
    @Column(nullable = false) //los valores no pueden ser nulos
    String username;
    String dni;
    String password;
    String fullName;
    String address;
    String phoneNumber;

    @Enumerated(EnumType.STRING) //campo enumerado que se guarda como texto en la BD.
    @JsonIgnore
    Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Cart cart; // Relación de uno a uno con la entidad Cart


    //Define los roles o permisos de los usuarios. Spring Security gestiona los permisos.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of(new SimpleGrantedAuthority((role.name())));
    }


    //Retornan true para que Spring Security identifique que las cuentas están activas
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }

}
