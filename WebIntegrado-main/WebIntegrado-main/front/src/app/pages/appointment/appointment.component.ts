import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AppointmentService } from 'src/app/services/appointment/appointment.service';
import { PetService } from 'src/app/services/pet/pet.service';
import { ServiceService } from 'src/app/services/service/service.service';
import { LoginService } from 'src/app/services/auth/login.service';
import { Pet } from 'src/app/models/pet.model';
import { Service } from 'src/app/models/service.model';
import { AppointmentRequest } from 'src/app/models/appointment-request.model';

@Component({
  selector: 'app-appointment',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './appointment.component.html',
  styleUrl: './appointment.component.css'
})
export class AppointmentComponent implements OnInit {
  appointmentForm: FormGroup;
  pets: Pet[] = [];
  services: Service[] = [];
  errorMessage: string = '';
  successMessage: string = '';
  isLoading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private appointmentService: AppointmentService,
    private petService: PetService,
    private serviceService: ServiceService,
    private loginService: LoginService,
    private router: Router
  ) {
    this.appointmentForm = this.fb.group({
      petId: ['', Validators.required],
      serviceId: ['', Validators.required],
      appointmentDate: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadUserPets();
    this.loadServices();
    this.setMinDate();
  }

  loadUserPets(): void {
    const userId = this.loginService.userId;
    if (userId) {
      this.petService.getPetsByUserId(parseInt(userId)).subscribe({
        next: (pets) => {
          this.pets = pets;
          if (pets.length === 0) {
            this.errorMessage = 'No tienes mascotas registradas. Por favor, registra una mascota primero.';
          }
        },
        error: (error) => {
          console.error('Error al cargar mascotas:', error);
          this.errorMessage = 'Error al cargar tus mascotas. Por favor, intenta de nuevo.';
        }
      });
    } else {
      this.errorMessage = 'Debes iniciar sesión para reservar una cita.';
    }
  }

  loadServices(): void {
    this.serviceService.getAllServices().subscribe({
      next: (services) => {
        this.services = services;
      },
      error: (error) => {
        console.error('Error al cargar servicios:', error);
        this.errorMessage = 'Error al cargar los servicios disponibles.';
      }
    });
  }

  setMinDate(): void {
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);
    const minDate = tomorrow.toISOString().split('T')[0];
    const dateInput = document.querySelector('input[type="date"]') as HTMLInputElement;
    if (dateInput) {
      dateInput.setAttribute('min', minDate);
    }
  }

  get petId() {
    return this.appointmentForm.get('petId');
  }

  get serviceId() {
    return this.appointmentForm.get('serviceId');
  }

  get appointmentDate() {
    return this.appointmentForm.get('appointmentDate');
  }

  get startTime() {
    return this.appointmentForm.get('startTime');
  }

  get endTime() {
    return this.appointmentForm.get('endTime');
  }

  onSubmit(): void {
    if (this.appointmentForm.invalid) {
      this.markFormGroupTouched(this.appointmentForm);
      return;
    }

    const userId = this.loginService.userId;
    if (!userId) {
      this.errorMessage = 'Debes iniciar sesión para reservar una cita.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const formValue = this.appointmentForm.value;
    const appointmentRequest: AppointmentRequest = {
      userId: parseInt(userId),
      petId: formValue.petId,
      serviceId: formValue.serviceId,
      appointmentDate: formValue.appointmentDate,
      startTime: formValue.startTime,
      endTime: formValue.endTime,
      status: 'PENDIENTE'
    };

    this.appointmentService.createAppointment(appointmentRequest).subscribe({
      next: (response) => {
        this.successMessage = '¡Cita reservada exitosamente!';
        this.appointmentForm.reset();
        setTimeout(() => {
          this.router.navigate(['/dashboard']);
        }, 2000);
      },
      error: (error) => {
        console.error('Error al reservar cita:', error);
        this.errorMessage = error.error?.message || 'Error al reservar la cita. Por favor, intenta de nuevo.';
        this.isLoading = false;
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
    });
  }

  getSelectedPet(): Pet | undefined {
    const petId = this.appointmentForm.get('petId')?.value;
    return this.pets.find(p => p.id === petId);
  }

  getSelectedService(): Service | undefined {
    const serviceId = this.appointmentForm.get('serviceId')?.value;
    return this.services.find(s => s.id === serviceId);
  }
}
