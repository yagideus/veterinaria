import { Component } from '@angular/core';
import { NavComponent } from 'src/app/shared/nav/nav.component';
import { FooterComponent } from 'src/app/shared/footer/footer.component';
import { AppointmentComponent } from './appointment.component';

@Component({
    selector: 'app-appointment-page',
    standalone: true,
    imports: [NavComponent, FooterComponent, AppointmentComponent],
    template: `
    <app-nav></app-nav>
    <app-appointment></app-appointment>
    <app-footer></app-footer>
  `
})
export class AppointmentPageComponent { }
