export interface SaleDetail {
    idDetail: number;
    quantity: number;
    productId: number;
    unitPrice: number;
    productName: string;
    userFullName: string;
}

export interface SaleDetailsResponse {
    saleDetails: SaleDetail[];
}