import { TuiRoot } from '@taiga-ui/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { ShellComponent } from './shell/shell.component';

@Component({
  selector: 'app-root',
  imports: [TuiRoot, ShellComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.less',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppComponent {}
