import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { NavComponent } from '../../shared/nav/nav.component';
import { Appointment } from 'src/app/models/appointment.model';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AppointmentService } from 'src/app/services/appointment/appointment.service';

@Component({
  selector: 'app-appointment',
  standalone: true,
  imports: [NavComponent, CommonModule, ReactiveFormsModule],
  templateUrl: './appointment.component.html',
  styleUrls: ['../../admin.component.css'],
})
export class AppointmentComponent implements OnInit {
  appointments: Appointment[] = [];
  showAddForm: boolean = false;
  selectedAppointment: Appointment | null = null;
  appointmentForm: FormGroup;

  constructor(
    private appointmentService: AppointmentService,
    private fb: FormBuilder
  ) {
    this.appointmentForm = this.fb.group({
      userId: ['', Validators.required],
      serviceId: ['', Validators.required],
      petId: ['', Validators.required],
      appointmentDate: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      status: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.loadAppointments();
  }

  loadAppointments(): void {
    this.appointmentService.getAllAppointments().subscribe(
      (data) => {
        this.appointments = data;
      },
      (error) => {
        console.error('Error al cargar las mascotas:', error);
      }
    );
  }

  editAppointment(appointment: Appointment): void {
    this.selectedAppointment = appointment;
    this.showAddForm = true;
    this.appointmentForm.patchValue(appointment);
  }

  deleteAppointment(id: number): void {
    if (confirm('¿Estás seguro de que quieres eliminar esta cita?')) {
      this.appointmentService.deleteAppointment(id).subscribe(
        () => {
          this.loadAppointments(); // Refresh the list after deletion
        },
        (error) => {
          console.error('Error deleting appointment', error);
        }
      );
    }
  }

  cancel(): void {
    this.showAddForm = false;
    this.selectedAppointment = null;
    this.appointmentForm.reset();
  }

  saveAppointment(): void {
    if (this.appointmentForm.invalid) return;

    const appointmentData = this.appointmentForm.value;

    if (this.selectedAppointment) {
      this.appointmentService
        .updateAppointment(this.selectedAppointment.id, appointmentData)
        .subscribe(
          () => {
            this.loadAppointments();
            this.cancel();
          },
          (error) => {
            console.error('Error al actualizar la mascota:', error);
          }
        );
    } else {
      this.appointmentService.createAppointment(appointmentData).subscribe(
        () => {
          this.loadAppointments();
          this.cancel();
        },
        (error) => {
          console.error('Error al agregar la mascota:', error);
        }
      );
    }

    
  }

  showAddAppointmentForm(): void {
    this.showAddForm = true;
    this.selectedAppointment = null;
    this.appointmentForm.reset();
  }
}
