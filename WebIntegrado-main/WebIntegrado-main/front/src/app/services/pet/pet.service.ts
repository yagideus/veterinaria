import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Pet } from 'src/app/models/pet.model';

@Injectable({
  providedIn: 'root'
})
export class PetService {

  private baseUrl = 'http://localhost:8080/pets'; 

  constructor(private http: HttpClient) {}

  // Obtener todas las mascotas
  getAllPets(): Observable<Pet[]> {
    return this.http.get<Pet[]>(`${this.baseUrl}`);
  }

  // Obtener una mascota por ID
  getPetById(id: number): Observable<Pet> {
    return this.http.get<Pet>(`${this.baseUrl}/${id}`);
  }

  // Crear una nueva mascota
  createPet(pet: Partial<Pet>): Observable<Pet> {
    return this.http.post<Pet>(`${this.baseUrl}/create`, pet);
  }

  // Actualizar una mascota por ID
  updatePet(id: number, pet: Partial<Pet>): Observable<Pet> {
    return this.http.put<Pet>(`${this.baseUrl}/${id}`, pet);
  }

  // Eliminar una mascota por ID
  deletePet(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  // Obtener todas las mascotas de un usuario por ID
  getPetsByUserId(userId: number): Observable<Pet[]> {
    return this.http.get<Pet[]>(`${this.baseUrl}/user/${userId}`);
  }
}
