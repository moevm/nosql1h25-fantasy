import { Component } from '@angular/core';
import { AppHeaderComponent } from '../app-header/app-header.component';
import { FooterComponent } from '../footer/footer.component';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-shell',
  imports: [AppHeaderComponent, FooterComponent, RouterOutlet],
  templateUrl: './shell.component.html',
  styleUrl: './shell.component.less',
})
export class ShellComponent {}
