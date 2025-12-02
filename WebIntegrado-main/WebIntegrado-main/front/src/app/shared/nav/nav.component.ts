import { NgIf } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { LoginService } from 'src/app/services/auth/login.service';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css'],
  standalone: true,
  imports: [RouterLink, RouterLinkActive, NgIf]
})
export class NavComponent implements OnInit {
  userLoginOn: boolean = false;
  isAdmin: boolean = false;

  constructor(private loginService: LoginService, private router: Router) { }

  ngOnInit(): void {
    this.loginService.currentUserLoginOn.subscribe(
      {
        next: (userLoginOn) => {
          this.userLoginOn = userLoginOn;
          this.checkAdminRole();
        }
      }
    );

    this.checkAdminRole();
  }

  checkAdminRole(): void {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decodedToken: any = jwtDecode(token); // Decodificar el token
        this.isAdmin = decodedToken?.role.includes('ADMIN'); // Comprobar si el rol es 'ADMIN'
      } catch (error) {
        console.error('Error decodificando el token:', error);
        this.isAdmin = false;
      }
    } else {
      this.isAdmin = false; // Si no hay token, no es admin
    }
  }

  logout() {
    this.loginService.logout();
    this.router.navigate(['/iniciar-sesion'])
  }

}
