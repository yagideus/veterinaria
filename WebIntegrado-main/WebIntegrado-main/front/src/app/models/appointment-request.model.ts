export interface AppointmentRequest {
  userId: number;
  petId: number;
  serviceId: number;
  appointmentDate: string;
  startTime: string;
  endTime: string;
  status?: string;
}

