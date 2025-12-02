import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Medicine } from 'src/app/models/medicine.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MedicineService {

  private apiUrl = `${environment.urlApi}medicine`;

  constructor(private http:HttpClient) { }

   // Obtener todos los productos
   getAllMedicine(): Observable<Medicine[]> {
    return this.http.get<Medicine[]>(this.apiUrl);
  }

  // Obtener un producto por ID
  getMedicineById(id: number): Observable<Medicine> {
    return this.http.get<Medicine>(`${this.apiUrl}/${id}`);
  }

  // Crear un nuevo producto
  createMedicine(medicine: Medicine): Observable<Medicine> {
    return this.http.post<Medicine>(this.apiUrl, medicine);
  }

  // Actualizar un producto existente
  updateMedicine(id: number, medicine: Medicine): Observable<Medicine> {
    return this.http.put<Medicine>(`${this.apiUrl}/${id}`, medicine);
  }

  // Eliminar un producto por ID
  deleteMedicine(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Descargar el archivo Excel
  downloadExcel(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export.xlsx`, {
      responseType: 'blob'
    });
  }
}
