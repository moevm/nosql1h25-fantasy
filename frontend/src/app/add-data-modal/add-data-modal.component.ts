import {
  ChangeDetectionStrategy,
  Component,
  inject,
} from '@angular/core';
import {
  TuiButton,
  TuiError,
  TuiTextfield,
  TuiTextfieldComponent,
  TuiTextfieldDirective,
  TuiTextfieldOptionsDirective,
  TuiTitle,
} from '@taiga-ui/core';
import {
  TuiInputModule,
  TuiInputRangeModule,
  TuiInputTagModule,
  TuiSelectModule,
  TuiTextfieldControllerModule,
} from '@taiga-ui/legacy';
import {
  TUI_DEFAULT_MATCHER,
  tuiPure,
  TuiValidationError,
} from '@taiga-ui/cdk';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  TuiChevron,
  TuiFieldErrorPipe,
  TuiInputNumber,
  TuiSlider,
} from '@taiga-ui/kit';
import { TuiForm } from '@taiga-ui/layout';
import { AsyncPipe, NgForOf, NgIf } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-add-data-modal',
  imports: [
    TuiInputModule,
    TuiSelectModule,
    TuiTextfieldControllerModule,
    FormsModule,
    TuiSlider,
    TuiButton,
    ReactiveFormsModule,
    TuiForm,
    TuiTextfieldComponent,
    TuiTextfieldDirective,
    TuiTextfieldOptionsDirective,
    TuiTitle,
    TuiChevron,
    TuiTextfield,
    TuiInputRangeModule,
    TuiInputNumber,
    TuiInputTagModule,
    NgIf,
    TuiError,
    TuiFieldErrorPipe,
    AsyncPipe,
    NgForOf,
  ],
  templateUrl: './add-data-modal.component.html',
  styleUrl: './add-data-modal.component.less',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddDataModalComponent {
  private http = inject(HttpClient);
  private url = 'http://localhost:8080/catalog';
  private allowedTags = [
    'FANTASY',
    'SCIENCE FICTION',
    'DRAMA',
    'HOLLYWOOD',
    'FAMILY',
  ];

  readonly form = new FormGroup({
    type: new FormControl<string>('BOOK', Validators.required),
    title: new FormControl<string>('', Validators.required),
    description: new FormControl<string>('', Validators.required),
    yearRange: new FormControl<number[]>(
      [1800, 2025],
      Validators.required
    ),
    rating: new FormControl<number>(10, Validators.required),
    country: new FormControl<string>('', Validators.required),
    duration: new FormControl<number>(0, Validators.required),
    tags: new FormControl<string[]>([], control => {
      const userTags: string[] = control.getRawValue();
      const invalidTags = userTags.filter(
        (tag: string) => !this.allowedTags.includes(tag.toUpperCase())
      );

      return invalidTags.length > 0
        ? {
            tags: new TuiValidationError('Some tags are invalid'),
          }
        : null;
    }),
    authors: new FormControl<string[]>(['ExampleAuthor']),
    directors: new FormControl<string[]>(['ExampleDirector']),
    actors: new FormControl<string[]>(['ExampleActor']),
  });

  protected readonly types = ['BOOK', 'SERIES', 'FILM'];
  protected search = '';

  protected readonly countries = [
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

  protected get filtered(): readonly string[] {
    return this.filterBy(
      this.search,
      this.form.getRawValue().tags ?? []
    );
  }

  @tuiPure
  private filterBy(
    search: string,
    value: readonly string[]
  ): readonly string[] {
    return this.allowedTags.filter(
      tag => TUI_DEFAULT_MATCHER(tag, search) && !value.includes(tag)
    );
  }

  protected submit() {
    if (this.form.valid) {
      const data = this.form.getRawValue();

      const persons = [];

      if (data.type === 'BOOK') {
        for (const author of data.authors ? data.authors : [])
          persons.push({ name: author, role: 'AUTHOR' });
      }
      if (data.type === 'FILM' || data.type === 'SERIES') {
        for (const director of data.directors ? data.directors : [])
          persons.push({ name: director, role: 'DIRECTOR' });
        for (const actor of data.actors ? data.actors : [])
          persons.push({ name: actor, role: 'ACTOR' });
      }

      const newObject = {
        title: data.title,
        type: data.type,
        description: data.description,
        startYear: data.yearRange ? data.yearRange[0] : 1800,
        endYear: data.yearRange ? data.yearRange[1] : 2025,
        tags: data.tags
          ? data.tags.map(tag =>
              tag.toUpperCase().replace(/\s+/g, '_')
            )
          : [],
        rating: data.rating,
        country: data.country ? data.country.toUpperCase() : null,
        duration: data.type === 'FILM' ? data.duration : null,
        quantityPages: data.type === 'BOOK' ? data.duration : null,
        seasons: data.type === 'SERIES' ? data.duration : null,
        persons: persons,
        reviews: [],
      };

      console.log(newObject);

      this.http
        .post(this.url, newObject, { observe: 'response' })
        .subscribe(res => {
          console.log('Response status:', res.status);
          console.log('Response body:', res.body);
        });
    }
  }
}
