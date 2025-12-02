import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Pet } from 'src/app/models/pet.model';
import { PetService } from 'src/app/services/pet/pet.service';
import { NavComponent } from '../../shared/nav/nav.component';

@Component({
  selector: 'app-pets',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NavComponent],
  templateUrl: './pets.component.html',
  styleUrl: './pets.component.css'
})
export class PetsComponent implements OnInit {

  pets: Pet[] = [];
  petForm: FormGroup;
  selectedPet: Pet | null = null;
  showAddForm: boolean = false;

  constructor(private petService: PetService, private fb: FormBuilder) {
    this.petForm = this.fb.group({
      name: ['', Validators.required],
      species: ['', Validators.required],
      breed: [''],
      size: [''],
      weight: [0, [Validators.required, Validators.min(0)]],
      age: [0, [Validators.required, Validators.min(0)]],
      gender: [''],
      userId:[0, Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadPets();
  }

  // Cargar la lista de mascotas
  loadPets(): void {
    this.petService.getAllPets().subscribe(
      (data) => {
        this.pets = data;
      },
      (error) => {
        console.error('Error al cargar las mascotas:', error);
      }
    );
  }

  // Mostrar formulario de agregar mascota
  showAddPetForm(): void {
    this.showAddForm = true;
    this.selectedPet = null;
    this.petForm.reset();
  }

  // Agregar o actualizar mascota
  savePet(): void {
    if (this.petForm.invalid) return;

    const petData = this.petForm.value;

    if (this.selectedPet) {
      this.petService.updatePet(this.selectedPet.id, petData).subscribe(
        () => {
          this.loadPets();
          this.cancel();
        },
        (error) => {
          console.error('Error al actualizar la mascota:', error);
        }
      );
    } else {
      this.petService.createPet(petData).subscribe(
        () => {
          this.loadPets();
          this.cancel();
        },
        (error) => {
          console.error('Error al agregar la mascota:', error);
        }
      );
    }
  }

  // Editar mascota
  editPet(pet: Pet): void {
    this.selectedPet = pet;
    this.showAddForm = true;
    this.petForm.patchValue(pet);
  }

  // Eliminar mascota
  deletePet(id: number): void {
    if (confirm('¿Estás seguro de que deseas eliminar esta mascota?')) {
      this.petService.deletePet(id).subscribe(
        () => {
          this.loadPets();
        },
        (error) => {
          console.error('Error al eliminar la mascota:', error);
        }
      );
    }
  }

  // Cancelar acción
  cancel(): void {
    this.showAddForm = false;
    this.selectedPet = null;
    this.petForm.reset();
  }

}
