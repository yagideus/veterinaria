import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavComponent } from '../../shared/nav/nav.component';
import { SaleService } from 'src/app/services/sale/sale.service';
import { SaleDetail } from 'src/app/models/saleDetail.model';
import { FormsModule} from '@angular/forms';
@Component({
  selector: 'app-sales',
  standalone: true,
  imports: [CommonModule, NavComponent, FormsModule],
  templateUrl: './sales.component.html',
  styleUrls: ['../../admin.component.css'],
})
export class SalesComponent implements OnInit {
  sales: any[] = [];
  selectedSaleDetails: SaleDetail[] = [];
  selectedDetail: any = null;
  showModal = false;

  @ViewChild('saleDetailsModal') saleDetailsModal: any; // Referencia al modal

  constructor(private saleService: SaleService) {}

  ngOnInit(): void {
    this.saleService.getAllSales().subscribe(
      (data) => {
        this.sales = data;
      },
      (error) => {
        console.error('Error fetching sales data', error);
      }
    );

    if (this.selectedSaleDetails.length > 0) {
      this.selectedDetail = this.selectedSaleDetails[0];
    }

  }


  // Método para ver los detalles de una venta
  viewSaleDetails(saleId: number) {
    this.saleService.getSaleDetails(saleId).subscribe((data) => {
      this.selectedSaleDetails = data; // Asigna la respuesta al array tipado
      this.showModal = true; // Muestra el modal
    });
  }

  closeModal() {
    this.showModal = false; // Oculta el modal
    this.selectedSaleDetails = []; // Limpia los detalles seleccionados
  }

  downloadExcelFile(): void {
    this.saleService.downloadExcel().subscribe((blob: Blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'Ventas.xlsx';
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);
    });
  }
}
