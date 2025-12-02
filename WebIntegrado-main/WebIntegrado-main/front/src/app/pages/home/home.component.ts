import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { FooterComponent } from 'src/app/shared/footer/footer.component';
import { HeaderComponent } from 'src/app/shared/header/header.component';
import { NavComponent } from 'src/app/shared/nav/nav.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, HttpClientModule, NavComponent, HeaderComponent, FooterComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  productSelect: String = '';

  servicios = [
    {imagen: 'assets/images/Peluqueria.jpg', nombre: 'Grooming (Baño y Corte)', descripcion: 'Diagnosticamos problemas internos en tu mejor amigo, como fracturas o enfermedades, proporcionando una cliente clara de su estado de salud', precio: 'S/50 - S/80'},
    {imagen: 'assets/images/radiografia.jpg', nombre: 'Radiografía', descripcion: 'Diagnosticamos problemas internos en tu mejor amigo, como fracturas o enfermedades, proporcionando una cliente clara de su estado de salud.', precio: 'S/100 - S/150'},
    {imagen: 'assets/images/vacuna-gato.jpg', nombre: 'Vacunación', descripcion: 'Protegemos a tu mascota de enfermedades graves mediante la administración de dosis según su edad y especie, garantizando su salud y previniendo contagios.', precio: 'S/40 - S/70'},
    {imagen: 'assets/images/desparacitar.jpg', nombre: 'Desparacitación', descripcion: 'Eliminamos los parásitos internos y externos de tus engreídos, protegiendo su salud y previniendo infecciones que afectan su bienestar.', precio: 'S/30 - S/50'},
    {imagen: 'assets/images/cirugia.jpg', nombre: 'Cirugía', descripcion: 'Tratamos lesiones y enfermedades en tu mascota, solucionando problemas de salud críticos con procedimientos especializados.', precio: 'S/400 - S/1200'},
    {imagen: 'assets/images/laboratorio.jpg', nombre: 'Laboratorio', descripcion: 'Analizamos muestras de sangre, orina y heces para detectar enfermedades y monitorear la salud de tus amigos peludos.', precio: 'S/80 - S/150'}
    
  ]

  clientes = [
    {imagen: 'assets/images/cliente1.jpg'},
    {imagen: 'assets/images/cliente2.jpg'},
    {imagen: 'assets/images/cliente3.jpg'},
    {imagen: 'assets/images/cliente4.jpg'},
    {imagen: 'assets/images/cliente5.jpg'},
    {imagen: 'assets/images/cliente6.jpg'},
    {imagen: 'assets/images/cliente7.jpg'},
  ]

}
