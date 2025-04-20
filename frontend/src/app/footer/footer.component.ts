import { Component } from '@angular/core';
import { TuiButton, TuiGroup } from '@taiga-ui/core';

@Component({
  selector: 'app-footer',
  imports: [TuiGroup, TuiButton],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.less',
})
export class FooterComponent {}
