import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Food } from 'src/app/models/food.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FoodService {

  private apiUrl = `${environment.urlApi}food`;

  constructor(private http: HttpClient) { }

  // Obtener todos los productos
  getAllFood(): Observable<Food[]> {
    return this.http.get<Food[]>(this.apiUrl);
  }

  // Obtener un producto por ID
  getFoodById(id: number): Observable<Food> {
    return this.http.get<Food>(`${this.apiUrl}/${id}`);
  }

  // Crear un nuevo producto
  createFood(food: Food): Observable<Food> {
    return this.http.post<Food>(this.apiUrl, food);
  }

  // Actualizar un producto existente
  updateFood(id: number, food: Food): Observable<Food> {
    return this.http.put<Food>(`${this.apiUrl}/${id}`, food);
  }

  // Eliminar un producto por ID
  deleteFood(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Descargar el archivo Excel
  downloadExcel(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export.xlsx`, {
      responseType: 'blob'
    });
  }
}
