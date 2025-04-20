import { Component } from '@angular/core';
import { TuiForm, TuiHeader } from '@taiga-ui/layout';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';
import { TuiButton, TuiTextfield, TuiTitle } from '@taiga-ui/core';
import { TuiFilter } from '@taiga-ui/kit';
import {
  TuiInputDateModule,
  TuiInputPhoneModule,
  TuiSelectModule,
  TuiTextfieldControllerModule,
} from '@taiga-ui/legacy';

@Component({
  selector: 'app-filters',
  imports: [
    ReactiveFormsModule,
    TuiTextfield,
    TuiButton,
    TuiForm,
    TuiHeader,
    TuiTitle,
    TuiInputDateModule,
    TuiTextfieldControllerModule,
    TuiSelectModule,
    TuiInputPhoneModule,
    TuiFilter,
  ],
  templateUrl: './filters.component.html',
  styleUrl: './filters.component.less',
})
export class FiltersComponent {
  protected readonly form = new FormGroup({
    search: new FormControl(),
    tagFilter: new FormControl(),
    typeFilter: new FormControl(),
  });

  protected readonly tagFilters = [
    'Adventure',
    'Action',
    'Drama',
    'Hollywood',
    'Family',
  ];

  protected readonly typeFilters = ['Type', 'Movies', 'TV Shows'];

  selectAllTags(): void {
    this.form.get('tagFilter')?.setValue(this.tagFilters);
  }
}
