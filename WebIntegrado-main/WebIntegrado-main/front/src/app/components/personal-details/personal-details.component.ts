import { Component } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { LoginService } from 'src/app/services/auth/login.service';
import { User } from 'src/app/services/auth/user';
import { UserService } from 'src/app/services/user/user.service';
import { NgIf } from '@angular/common';

@Component({
    selector: 'app-personal-details',
    templateUrl: './personal-details.component.html',
    styleUrls: ['./personal-details.component.css'],
    standalone: true,
    imports: [NgIf, ReactiveFormsModule]
})
export class PersonalDetailsComponent  {
  errorMessage: string = "";
  user?: User;
  userLoginOn: boolean = false;
  editMode: boolean = false;

  registerForm = this.formBuilder.group({
    id: [''],
    username: ['', Validators.required], // Agregado el campo username
    fullName: ['', Validators.required],
    dni: ['', Validators.required],
    address: ['', Validators.required],
    phoneNumber: ['', Validators.required],
  });

  constructor(private userService: UserService, private formBuilder: FormBuilder, private loginService: LoginService) {
    const userId = sessionStorage.getItem("userId"); // Obtener userId de sessionStorage

    if (userId) {
      this.loadUserData(+userId);
    } else {
      this.errorMessage = "No se pudo obtener el ID del usuario.";
    }

    this.loginService.userLoginOn.subscribe({
      next: (userLoginOn) => {
        this.userLoginOn = userLoginOn;
      }
    });
  }

  private loadUserData(userId: number): void {
    this.userService.getUser(userId).subscribe({
      next: (userData) => {
        this.user = userData;
        this.registerForm.patchValue({
          id: userData.id.toString(),
          username: userData.username, // Cargar el valor de username
          fullName: userData.fullName,
          dni: userData.dni,
          address: userData.address,
          phoneNumber: userData.phoneNumber,
        });
      },
      error: (errorData) => {
        this.errorMessage = 'Error al obtener datos del usuario: ' + errorData;
      },
      complete: () => {
        console.info("User Data loaded successfully");
      }
    });
  }

  get fullName() {
    return this.registerForm.controls.fullName;
  }

  get dni() {
    return this.registerForm.controls.dni;
  }

  get address() {
    return this.registerForm.controls.address;
  }

  get phoneNumber() {
    return this.registerForm.controls.phoneNumber;
  }

  get username() {
    return this.registerForm.controls.username; // Getter para username
  }

  updateUserDetails(): void {
    if (this.registerForm.valid && this.user && this.user.id) {
      const updatedUser: User = {
        id: this.user.id,
        username: this.registerForm.value.username || '', // Asegurarse de que username esté incluido
        fullName: this.registerForm.value.fullName || '',
        dni: this.registerForm.value.dni || '',
        address: this.registerForm.value.address || '',
        phoneNumber: this.registerForm.value.phoneNumber || '',
        role: this.user.role // Asegúrate de que estás copiando el campo role
      };

      this.userService.updateUser(updatedUser.id, updatedUser).subscribe({
        next: () => {
          this.editMode = false;
          this.user = updatedUser; // Actualiza la información localmente
          console.info('User updated successfully');
        },
        error: (errorData) => {
          this.errorMessage = 'Error al actualizar el usuario: ' + errorData;
          console.error('Update error:', errorData);
        }
      });
    } else {
      this.errorMessage = 'Por favor, complete todos los campos obligatorios.';
      console.warn('Form is invalid or user ID is undefined');
    }
  }
}
