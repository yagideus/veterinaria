export interface Food {
    id?: number; // Opcional, ya que puede no estar presente en un nuevo objeto antes de guardarse en la base de datos
    name: string;
    brand?: string;
    description?: string;
    price: number;
    stock: number;
    category?: string;
    image_url?: string;
    expiration_date?: Date; // Tipo string para manejar formato de fecha
    created_at?: Date;
    productType?: string;
  }