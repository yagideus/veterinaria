export interface CartItem {
  idCartItem: number;
  cart: { idCart: number }; // Para simplificar, solo guardamos el id del carrito
  product: { idProduct: number, name: String, image_url: String }; // Para simplificar, solo guardamos el id del producto
  productType: string;
  quantity: number;
  price: number;
}
