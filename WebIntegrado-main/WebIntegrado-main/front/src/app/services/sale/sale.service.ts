import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Sale } from 'src/app/models/sale.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class SaleService {
  private apiUrl = `${environment.urlHost}sales`;

  constructor(private http: HttpClient) {}

  getAllSales(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  createSale(
    userId: number,
    cartItems: any[],
    totalAmount: number
  ): Observable<Sale> {
    const saleData = {
      userId: userId,
      saleDetails: cartItems.map((item) => ({
        productId: item.product.id,
        quantity: item.quantity,
        unitPrice: item.price,
      })),
      totalAmount: totalAmount,
    };

    return this.http.post<Sale>(`${this.apiUrl}/create`, saleData);
  }

  getSaleDetails(saleId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${saleId}`);
  }

   // Crear una orden de PayPal en el backend
   createPaypalOrder(totalAmount: number): Observable<any> {
    const orderData = { totalAmount: totalAmount };
    return this.http.post<any>(`${environment.urlHost}/api/paypal/create-order`, orderData);
  }

  // Capturar la orden de PayPal después de la aprobación
  capturePaypalOrder(orderId: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/paypal/capture-order`, { orderId });
  }

  // Descargar el archivo Excel
  downloadExcel(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export.xlsx`, {
      responseType: 'blob'
    });
  }
}
