import { Component } from '@angular/core';
import { FooterComponent } from 'src/app/shared/footer/footer.component';
import { HeaderComponent } from 'src/app/shared/header/header.component';
import { NavComponent } from 'src/app/shared/nav/nav.component';

@Component({
  selector: 'app-secondary',
  standalone: true,
  imports: [NavComponent,HeaderComponent,FooterComponent],
  templateUrl: './secondary.component.html',
  styleUrl: './secondary.component.css'
})
export class SecondaryComponent {

}
