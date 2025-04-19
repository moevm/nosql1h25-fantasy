import { Component } from '@angular/core';
import { TuiGroup, TuiLink } from '@taiga-ui/core';

@Component({
  selector: 'app-footer',
  imports: [TuiGroup, TuiLink],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.less',
})
export class FooterComponent {}
