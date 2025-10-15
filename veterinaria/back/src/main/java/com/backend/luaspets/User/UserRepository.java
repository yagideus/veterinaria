package com.backend.luaspets.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


//Se define una interfaz de repositorio que extiende JpaRepository para acceder a los metodos CRUD (save(), findById(), findAll(), deleteById()) y de consulta auto. para
//trabajar con la entidad User, la cual tiene como llave primaria un Integer.
public interface UserRepository extends JpaRepository<User, Integer> {

    //Metodo personalizado: findByUsername
    //Optional para manejar valores que pueden ser nulos
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    @Modifying() //Indica consulta de modificación

    //Define una consulta JPQL (Java Persistence Query Language) personalizada updateUser para actualizar campos especificos
    @Query("update User u set u.fullName=:fullName, u.dni=:dni, u.address=:address, u.phoneNumber=:phoneNumber, u.role=:role where u.id=:id")
    void updateUser(
            @Param(value = "id") Integer id,
            @Param(value = "fullName") String fullName,
            @Param(value = "dni") String dni,
            @Param(value = "address") String address,
            @Param(value = "phoneNumber") String phoneNumber,
            @Param(value = "role") Role role);
}
