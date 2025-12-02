import { Component, OnInit } from '@angular/core';
import { NavComponent } from '../../shared/nav/nav.component';
import { Router} from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Accessories } from 'src/app/models/accessories.model';
import { AccessoriesService } from 'src/app/services/accessories/accessories.service';

@Component({
  selector: 'app-accesories',
  standalone: true,
  imports: [NavComponent, CommonModule, ReactiveFormsModule],
  templateUrl: './accessories.component.html',
  styleUrls: ['../../admin.component.css']
})
export class AccesoriesComponent implements OnInit {

  accessories: Accessories[] = []; // Lista de alimentos
  selectedAccessories: Accessories | null = null;
  accessoriesForm: FormGroup;
  showAddForm: boolean = false;
  errorMessage: string = ''; // Mensaje de error para mostrar en pantalla

  constructor(private accessoriesService: AccessoriesService, private router: Router, private formBuilder: FormBuilder) {
    // Inicializar el formulario para editar alimentos
    this.accessoriesForm = this.formBuilder.group({
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
    this.loadAccessoriess(); // Cargar todos los alimentos al iniciar el componente
  }

  // Método para cargar todos los alimentos
  loadAccessoriess(): void {
    this.accessoriesService.getAllAccessories().subscribe({
      next: (data) => (this.accessories = data),
      error: (err) => (this.errorMessage = 'Error al cargar los alimentos')
    });
  }

  // Método para mostrar el formulario de agregar
  showAddAccessoriesForm(): void {
    this.showAddForm = true; // Mostrar el formulario de agregar
    this.resetForm(); // Reiniciar el formulario
  }

  // Método para agregar un nuevo alimento
  addAccessories(): void {
    if (this.accessoriesForm.valid) {
      const accessoriesData = this.accessoriesForm.value;
      accessoriesData.productType = 'ACCESSORY'; // Añadir el tipo de producto
      if (accessoriesData.expiration_date === '') {
        accessoriesData.expiration_date = null;
      }
      if (accessoriesData.created_at === '') {
        accessoriesData.created_at = null;
      }
      this.accessoriesService.createAccessories(accessoriesData).subscribe({
        next: () => {
          this.loadAccessoriess(); // Recargar la lista después de agregar
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
  editAccessories(accessories: Accessories): void {
    this.selectedAccessories = accessories;
    this.accessoriesForm.patchValue(accessories); // Rellenar el formulario con los datos del alimento seleccionado
    this.showAddForm = false;
  }


// Método para eliminar un alimento
deleteAccessories(id: number | undefined): void {
  if (id !== undefined) {
    this.accessoriesService.deleteAccessories(id).subscribe({
      next: () => this.loadAccessoriess(),
      error: (err) => (this.errorMessage = 'Error al eliminar el alimento')
    });
  }
}

// Método para actualizar el alimento
updateAccessories(): void {
  if (this.selectedAccessories) {
    const accessoriesData = this.accessoriesForm.value;
    if (accessoriesData.expiration_date === '') {
      accessoriesData.expiration_date = null;
    }
    if (accessoriesData.created_at === '') {
      accessoriesData.created_at = null;
    }
    this.accessoriesService.updateAccessories(this.selectedAccessories.id!, accessoriesData).subscribe({
      next: () => {
        this.loadAccessoriess(); // Recargar la lista después de actualizar
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
  this.accessoriesForm.reset();
  this.selectedAccessories = null;
}


//Método para ocultar el formulario
cancel(): void {
  this.resetForm();
  this.showAddForm = false;
}

downloadExcelFile(): void {
  this.accessoriesService.downloadExcel().subscribe((blob: Blob) => {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'AlmacenAccesorios.xlsx';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  });
}
}
