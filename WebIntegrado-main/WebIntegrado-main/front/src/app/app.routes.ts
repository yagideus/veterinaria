
import { Routes } from '@angular/router';
import { authGuard } from './auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/iniciar-sesion', pathMatch: 'full' },
  { path: 'perfil', loadComponent: () => import('./pages/dashboard/dashboard.component').then(c => c.DashboardComponent) },
  { path: 'iniciar-sesion', loadComponent: () => import('./auth/login/login.component').then(c => c.LoginComponent) },
  { path: 'home', loadComponent: () => import('./pages/home/home.component').then(c => c.HomeComponent) },
  { path: 'petshop', loadComponent: () => import('./pages/shop/shop.component').then(c => c.ShopComponent) },
  { path: 'blog', loadComponent: () => import('./pages/blog/blog.component').then(c => c.BlogComponent) },
  { path: 'register', loadComponent: () => import('./auth/register/register.component').then(c => c.RegisterComponent) },
  { path: 'cart', loadComponent: () => import('./pages/cart/cart.component').then(c => c.CartComponent) },
  { path: 'appointment', loadComponent: () => import('./pages/appointment/appointment-page.component').then(c => c.AppointmentPageComponent) },


  /* Botones Blog Para Acceder al Secundario*/
  { path: 'secondary', loadComponent: () => import('./pages/secondary/secondary.component').then(c => c.SecondaryComponent) },

  /* Barra de Navegacion Intranet */
  { path: 'users', loadComponent: () => import('./admin/pages/users/users.component').then(c => c.UsersComponent), canActivate: [authGuard], data: { role: ['ADMIN'] } },
  { path: 'appointments', loadComponent: () => import('./admin/pages/appointment/appointment.component').then(c => c.AppointmentComponent), canActivate: [authGuard], data: { role: ['ADMIN'] } },
  { path: 'medicine', loadComponent: () => import('./admin/pages/medicine/medicine.component').then(c => c.MedicineComponent), canActivate: [authGuard], data: { role: ['ADMIN'] } },
  { path: 'food', loadComponent: () => import('./admin/pages/food/food.component').then(c => c.FoodComponent), canActivate: [authGuard], data: { role: ['ADMIN'] } },
  { path: 'accesories', loadComponent: () => import('./admin/pages/accessories/accessories.component').then(c => c.AccesoriesComponent), canActivate: [authGuard], data: { role: ['ADMIN'] } },
  { path: 'sales', loadComponent: () => import('./admin/pages/sales/sales.component').then(c => c.SalesComponent), canActivate: [authGuard], data: { role: ['ADMIN'] } },
  { path: 'pets', loadComponent: () => import('./admin/pages/pets/pets.component').then(c => c.PetsComponent), canActivate: [authGuard], data: { role: ['ADMIN'] } },
  { path: 'services', loadComponent: () => import('./admin/pages/services/services.component').then(c => c.ServicesComponent), canActivate: [authGuard], data: { role: ['ADMIN'] } },

];


