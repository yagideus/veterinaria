import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../../admin.component';
import { NavComponent } from '../../shared/nav/nav.component';
import { Router, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Food } from 'src/app/models/food.model';
import { FoodService } from 'src/app/services/food/food.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-food',
  standalone: true,
  imports: [NavComponent, CommonModule, ReactiveFormsModule],
  templateUrl: './food.component.html',
  styleUrls: ['../../admin.component.css']
})
export class FoodComponent implements OnInit {

  foods: Food[] = []; // Lista de alimentos
  selectedFood: Food | null = null;
  foodForm: FormGroup;
  showAddForm: boolean = false;
  errorMessage: string = ''; // Mensaje de error para mostrar en pantalla

  constructor(private foodService: FoodService, private router: Router, private formBuilder: FormBuilder) {
    // Inicializar el formulario para editar alimentos
    this.foodForm = this.formBuilder.group({
      id: [null],
      name: ['', Validators.required],
      brand: [''],
      description: [''],
      price: [0, [Validators.required, Validators.min(0)]],
      stock: [0, [Validators.required, Validators.min(0)]],
      category: [''],
      image_url: [''],
      expiration_date: [''],
      created_at: ['']
    });
  }

  ngOnInit(): void {
    this.loadFoods(); // Cargar todos los alimentos al iniciar el componente
  }

  // Método para cargar todos los alimentos
  loadFoods(): void {
    this.foodService.getAllFood().subscribe({
      next: (data) => (this.foods = data),
      error: (err) => (this.errorMessage = 'Error al cargar los alimentos')
    });
  }

  // Método para mostrar el formulario de agregar
  showAddFoodForm(): void {
    this.showAddForm = true; // Mostrar el formulario de agregar
    this.resetForm(); // Reiniciar el formulario
  }

  // Método para agregar un nuevo alimento
  addFood(): void {
    if (this.foodForm.valid) {
      const foodData = this.foodForm.value;
      foodData.productType = 'FOOD'; // Añadir el tipo de producto
      if (foodData.expiration_date === '') {
        foodData.expiration_date = null;
      }
      if (foodData.created_at === '') {
        foodData.created_at = null;
      }
      this.foodService.createFood(foodData).subscribe({
        next: () => {
          this.loadFoods(); // Recargar la lista después de agregar
          this.resetForm(); // Reiniciar el formulario
          this.showAddForm = false; // Ocultar el formulario de agregar
        },
        error: (err) => {
          console.error('Error al agregar el alimento:', err);
        }
      });
    }
  }

  // Método para editar un alimento específico
  editFood(food: Food): void {
    this.selectedFood = food;
    this.foodForm.patchValue(food); // Rellenar el formulario con los datos del alimento seleccionado
    this.showAddForm = false;
  }

  // Método para eliminar un alimento
  deleteFood(id: number | undefined): void {
    if (id !== undefined) {
      this.foodService.deleteFood(id).subscribe({
        next: () => this.loadFoods(),
        error: (err) => (this.errorMessage = 'Error al eliminar el alimento')
      });
    }
  }

  // Método para actualizar el alimento
  updateFood(): void {
    if (this.selectedFood) {
      const foodData = this.foodForm.value;
      if (foodData.expiration_date === '') {
        foodData.expiration_date = null;
      }
      if (foodData.created_at === '') {
        foodData.created_at = null;
      }
      this.foodService.updateFood(this.selectedFood.id!, foodData).subscribe({
        next: () => {
          this.loadFoods(); // Recargar la lista después de actualizar
          this.resetForm(); // Reiniciar el formulario
        },
        error: (err) => {
          console.error('Error al actualizar el alimento:', err);
        }
      });
    }
  }

  // Método para reiniciar el formulario
  resetForm(): void {
    this.foodForm.reset();
    this.selectedFood = null;
  }

  // Método para ocultar el formulario
  cancel(): void {
    this.resetForm();
    this.showAddForm = false;
  }

  // Método para descargar el archivo Excel
  downloadExcelFile(): void {
    this.foodService.downloadExcel().subscribe((blob: Blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'AlmacenComida.xlsx';
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);
    });
  }
}
