export interface Medicine {
    id?: number;
    name: string;
    brand?: string;
    description?: string;
    price: number;
    stock: number;
    category?: string;
    image_url?: string;
    expiration_date?: Date;
    created_at?: Date;
    productType?: string;
  }
  