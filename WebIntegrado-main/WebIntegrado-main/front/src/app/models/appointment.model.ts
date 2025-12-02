export interface Appointment {
  id: number; 
  user: { id: number; username: string; fullName: string; dni: string };
  service: { id: number; name: string; cost: number; petSize: string };
  pet: { id: number; name: string; species: string; breed: string; size: string };
  appointmentDate: string; 
  startTime: string; 
  endTime: string; 
  status: string; 
}