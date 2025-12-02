import { SaleItem } from "./saleItem.model";

export interface Sale {
    id: number; 
    userId: number;
    saleDetails: SaleItem[];
    saleStatus: 'PENDIENTE' | 'COMPLETADO';
    totalAmount: number;
  }