import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Accessories } from 'src/app/models/accessories.model';
import { Food } from 'src/app/models/food.model';
import { Medicine } from 'src/app/models/medicine.model';
import { AccessoriesService } from 'src/app/services/accessories/accessories.service';
import { LoginService } from 'src/app/services/auth/login.service';
import { CartService } from 'src/app/services/cart/cart.service';
import { FoodService } from 'src/app/services/food/food.service';
import { MedicineService } from 'src/app/services/medicine/medicine.service';
import { FooterComponent } from 'src/app/shared/footer/footer.component';
import { HeaderComponent } from 'src/app/shared/header/header.component';
import { NavComponent } from 'src/app/shared/nav/nav.component';

@Component({
  selector: 'app-shop',
  standalone: true,
  imports: [CommonModule, NavComponent, HeaderComponent, FooterComponent],
  templateUrl: './shop.component.html',
  styleUrl: './shop.component.css'
})
export class ShopComponent implements OnInit{

  alimentos: Food[] = [];
  accesorios: Accessories[] = [];
  medicamentos: Medicine[] = [];
  productSelect: string = '';

  constructor(
    private foodService: FoodService,
    private accessoriesService: AccessoriesService,
    private medicineService: MedicineService,
    private cartService: CartService,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {
    this.loadFoods();
    this.loadAccesories();
    this.loadMedicines();
  }

  loadFoods(): void {
    this.foodService.getAllFood().subscribe({
      next: (data) => (this.alimentos = data), error: () => {},
    });
  }

  loadAccesories(): void {
    this.accessoriesService.getAllAccessories().subscribe({
      next: (data) => (this.accesorios = data), error: () => {},
    });
  }

  loadMedicines(): void {
    this.medicineService.getAllMedicine().subscribe({
      next: (data) => (this.medicamentos = data), error: () => {},
    });
  }

  switchProduct(product: string): void {
    this.productSelect = product;
  }

  // Método para agregar un producto al carrito
  addToCart(product: any, quantity: number): void {
    const userId = this.loginService.userId;
    if (!userId) return;

    const userIdInt = parseInt(userId, 10);
    if (isNaN(userIdInt)) return;

    const itemData = {
      productId: product.id,
      productType: this.getProductType(product),
      quantity
    };

    this.cartService.addItemToCartByUserId(userIdInt, itemData).subscribe({
      next: () => {},
      error: () => {}
    });
  }
  
  getProductType(product: any): string {
    return product.productType || 'UNKNOWN';
  }
  
}
