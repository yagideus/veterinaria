import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class PaypalService {
  private baseUrl = 'http://localhost:8080/api/paypal';

  constructor(private http: HttpClient) {}

  private paymentData: any = null;


  setPaymentData(data: any): void {
    this.paymentData = data; // Guarda los detalles de la transacción, incluido el orderId
  }
  
  getOrderId(): string | null {
    return this.paymentData ? this.paymentData.id : null; // Devuelve el orderId almacenado
  }
}
