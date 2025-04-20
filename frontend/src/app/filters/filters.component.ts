import { Component } from '@angular/core';
import { TuiForm, TuiHeader } from '@taiga-ui/layout';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';
import { TuiButton, TuiTextfield, TuiTitle } from '@taiga-ui/core';
import { TuiChevron, TuiFilter } from '@taiga-ui/kit';
import {
  TuiInputDateModule,
  TuiInputDateRangeModule,
  TuiInputPhoneModule,
  TuiInputRangeModule,
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
    TuiInputDateRangeModule,
    TuiInputRangeModule,
    TuiChevron,
  ],
  templateUrl: './filters.component.html',
  styleUrl: './filters.component.less',
})
export class FiltersComponent {
  protected readonly form = new FormGroup({
    search: new FormControl(),
    tagFilter: new FormControl(),
    typeFilter: new FormControl(),
    rating: new FormControl([0, 10]),
    yearRange: new FormControl([1950, 2025]),
    duration: new FormControl([0, 300]),
    quantity: new FormControl([0, 2000]),
    seasons: new FormControl([0, 20]),
    author: new FormControl(),
    director: new FormControl(),
    actors: new FormControl(),
    country: new FormControl(),
  });

  protected readonly tagFilters = [
    'Adventure',
    'Action',
    'Drama',
    'Hollywood',
    'Family',
  ];

  protected readonly typeFilters = ['Type', 'Movies', 'TV Shows'];

  protected readonly items = [
    'USA',
    'UK',
    'Germany',
    'France',
    'Italy',
    'Spain',
    'Canada',
    'Australia',
    'Japan',
    'South Korea',
    'India',
    'China',
    'Russia',
    'Brazil',
  ];

  selectAllTags(): void {
    this.form.get('tagFilter')?.setValue(this.tagFilters);
  }
}
