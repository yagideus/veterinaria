import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { User } from 'src/app/services/auth/user';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  registerForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private router: Router
  ) {
    this.registerForm = this.formBuilder.group({
      fullName: [
        '', 
        [Validators.required, Validators.pattern('^[a-zA-Z\\s]+$')]
      ],
      dni: [
        '', 
        [Validators.required, Validators.pattern('^[0-9]+$')]
      ],
      username: ['', [Validators.required, Validators.email]],
      address: ['', Validators.required],
      phoneNumber: [
        '', 
        [Validators.required, Validators.pattern('^[0-9]+$')]
      ],
      password: ['', Validators.required],
    });
  }

  get fullName() {
    return this.registerForm.get('fullName');
  }

  get dni() {
    return this.registerForm.get('dni');
  }

  get username() {
    return this.registerForm.get('username');
  }

  get address() {
    return this.registerForm.get('address');
  }

  get phoneNumber() {
    return this.registerForm.get('phoneNumber');
  }

  get password() {
    return this.registerForm.get('password');
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      const newUser: User = {
        id: 0, // Se asignará automáticamente en el backend
        role: 'user', // Por defecto
        ...this.registerForm.value,
      };

      this.userService.registerUser(newUser).subscribe({
        next: () => {
          this.router.navigate(['/iniciar-sesion']); // Redirigir a la página de inicio de sesión
        },
        error: (error) => {
          this.errorMessage = error.message || 'Ocurrió un error. Por favor, inténtelo de nuevo.';
        },
      });
    }
  }
}
