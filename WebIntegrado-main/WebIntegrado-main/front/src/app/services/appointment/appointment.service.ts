import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Appointment } from 'src/app/models/appointment.model';
import { AppointmentRequest } from 'src/app/models/appointment-request.model';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  private baseUrl = 'http://localhost:8080/appointments';

  constructor(private http: HttpClient) {}

 // Obtener todas las citas
 getAllAppointments(): Observable<Appointment[]> {
  return this.http.get<Appointment[]>(this.baseUrl);
}

// Obtener una cita por ID
getAppointmentById(id: number): Observable<Appointment> {
  return this.http.get<Appointment>(`${this.baseUrl}/${id}`);
}

// Crear una nueva cita
createAppointment(appointment: AppointmentRequest): Observable<Appointment> {
  return this.http.post<Appointment>(`${this.baseUrl}/create`, appointment);
}

// Actualizar una cita existente
updateAppointment(id: number, appointment: Partial<Appointment>): Observable<Appointment> {
  return this.http.put<Appointment>(`${this.baseUrl}/${id}`, appointment);
}

// Eliminar una cita por ID
deleteAppointment(id: number): Observable<void> {
  return this.http.delete<void>(`${this.baseUrl}/${id}`);
}
}
