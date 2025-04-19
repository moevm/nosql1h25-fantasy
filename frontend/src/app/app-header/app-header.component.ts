import { ChangeDetectionStrategy, Component } from '@angular/core';
import { TuiNavigation } from '@taiga-ui/layout';
import { TuiButton, TuiIcon, TuiTextfield } from '@taiga-ui/core';
import {
  TuiInputModule,
  TuiTextfieldControllerModule,
} from '@taiga-ui/legacy';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TuiTooltip } from '@taiga-ui/kit';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    TuiNavigation,
    TuiButton,
    TuiInputModule,
    ReactiveFormsModule,
    TuiTextfield,
    TuiTextfieldControllerModule,
    TuiIcon,
    TuiTooltip,
    FormsModule,
  ],
  selector: 'app-header',
  styleUrl: './app-header.component.less',
  templateUrl: './app-header.component.html',
})
export class AppHeaderComponent {
  protected value = '';
}
