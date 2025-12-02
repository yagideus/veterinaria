import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { getUserRoleFromToken } from './token-utils';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const token = localStorage.getItem('token');

  if (!token) {
      router.navigate(['/iniciar-sesion']);
      return false;
  }

  const userRole = getUserRoleFromToken(token);
  const allowedRoles = route.data?.['role'];

  if (allowedRoles && allowedRoles.includes(userRole)) {
      return true;
  }

  router.navigate(['/home']);
  return false;
};



