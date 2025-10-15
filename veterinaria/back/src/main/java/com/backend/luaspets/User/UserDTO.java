package com.backend.luaspets.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

//Se define la clase DTO (Data Transfer Object) para transferir los datos del usuario sin exponer la entidad completa User.
//Simplifica el intercambio de datos cuando no se necesita toda la información de una entidad o para filtrar ciertos datos por seguridad.
public class UserDTO {
    int id;
    String username;
    String dni;
    String fullName;
    String address;
    String phoneNumber;
    String role;
}
