import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Accessories } from 'src/app/models/accessories.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccessoriesService {

  private apiUrl = `${environment.urlApi}accessories`;

  constructor(private http:HttpClient) { }

  // Obtener todos los productos
  getAllAccessories(): Observable<Accessories[]> {
    return this.http.get<Accessories[]>(this.apiUrl);
  }

  // Obtener un producto por ID
  getAccesoriesById(id: number): Observable<Accessories> {
    return this.http.get<Accessories>(`${this.apiUrl}/${id}`);
  }

  // Crear un nuevo producto
  createAccessories(accessories: Accessories): Observable<Accessories> {
    return this.http.post<Accessories>(this.apiUrl, accessories);
  }

  // Actualizar un producto existente
  updateAccessories(id: number, accessories: Accessories): Observable<Accessories> {
    return this.http.put<Accessories>(`${this.apiUrl}/${id}`, accessories);
  }

  // Eliminar un producto por ID
  deleteAccessories(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  downloadExcel(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export.xlsx`, {
      responseType: 'blob'
    });
  }
}
