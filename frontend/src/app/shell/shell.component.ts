import { ChangeDetectionStrategy, Component } from '@angular/core';
import { AppHeaderComponent } from '../app-header/app-header.component';
import { FooterComponent } from '../footer/footer.component';
import { RouterOutlet } from '@angular/router';
import { TuiButton, tuiDialog } from '@taiga-ui/core';
import { AddDataModalComponent } from '../add-data-modal/add-data-modal.component';

@Component({
  selector: 'app-shell',
  imports: [
    AppHeaderComponent,
    FooterComponent,
    RouterOutlet,
    TuiButton,
  ],
  templateUrl: './shell.component.html',
  styleUrl: './shell.component.less',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
})
export class ShellComponent {
  private readonly dialog = tuiDialog(AddDataModalComponent, {
    dismissible: true,
    label: 'Add data',
  });

  protected showDialog(): void {
    const data = undefined;
    this.dialog(data).subscribe({
      complete: () => {
        console.info('Dialog closed');
      },
    });
  }
}
