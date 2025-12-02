import { CartItem } from "./cartItem.model";

export interface Cart {
    idCart: number;
    userId: number;
    items: CartItem[];
  }