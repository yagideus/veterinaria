import { ServiceService } from './../../../services/service/service.service';
import { Component, OnInit } from '@angular/core';
import { Service } from 'src/app/models/service.model';
import { NavComponent } from '../../shared/nav/nav.component';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-services',
  standalone: true,
  imports: [NavComponent, CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './services.component.html',
  styleUrl: './services.component.css'
})
export class ServicesComponent implements OnInit {

  services: Service[] = [];
  selectedService: Service | null = null;
  showAddForm: boolean = false;
  serviceForm: FormGroup;

  constructor(private serviceService: ServiceService, private fb: FormBuilder) {
    this.serviceForm = this.fb.group({
      name: ['', [Validators.required]],
      cost: [0, [Validators.required, Validators.min(0)]],
      petSize: [''],
    });
  }

  ngOnInit(): void {
    this.getAllServices();
  }

  getAllServices(): void {
    this.serviceService.getAllServices().subscribe((data) => {
      this.services = data;
    });
  }

  showAddServiceForm(): void {
    this.showAddForm = true;
    this.selectedService = null;
    this.serviceForm.reset();
  }

  editService(service: Service): void {
    this.selectedService = service;
    this.showAddForm = true;
    this.serviceForm.patchValue(service);
  }

  addOrUpdateService(): void {
    if (this.serviceForm.invalid) return;

    const serviceData: Service = this.serviceForm.value;

    if (this.selectedService) {
      // Update existing service
      this.serviceService.updateService(this.selectedService.id, serviceData).subscribe(() => {
        this.getAllServices();
        this.cancel();
      });
    } else {
      // Add new service
      this.serviceService.createService(serviceData).subscribe(() => {
        this.getAllServices();
        this.cancel();
      });
    }
  }

  deleteService(id: number): void {
    if (confirm('¿Estás seguro de que deseas eliminar este servicio?')) {
      this.serviceService.deleteService(id).subscribe(() => {
        this.getAllServices();
      });
    }
  }

  cancel(): void {
    this.showAddForm = false;
    this.selectedService = null;
    this.serviceForm.reset();
  }

}
