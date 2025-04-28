import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { TuiCardMedium, TuiForm, TuiHeader } from '@taiga-ui/layout';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';
import {
  TuiAppearance,
  TuiButton,
  TuiIcon,
  TuiTextfield,
  TuiTitle,
} from '@taiga-ui/core';
import { TuiAvatar, TuiChevron, TuiFilter } from '@taiga-ui/kit';
import {
  TuiInputDateModule,
  TuiInputDateRangeModule,
  TuiInputPhoneModule,
  TuiInputRangeModule,
  TuiSelectModule,
  TuiTextfieldControllerModule,
} from '@taiga-ui/legacy';
import { HttpClient } from '@angular/common/http';
import { DetailsComponent } from '../details/details.component';
import { NgIf } from '@angular/common';
import { Movie } from '../data-access/movie.service';
import { Book } from '../data-access/book.service';
import { Series } from '../data-access/series.service';
import { zip } from 'rxjs';

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
    DetailsComponent,
    NgIf,
    TuiAppearance,
    TuiAvatar,
    TuiCardMedium,
    TuiIcon,
    DetailsComponent,
  ],
  templateUrl: './filters.component.html',
  styleUrl: './filters.component.less',
  standalone: true,
})
export class FiltersComponent {
  constructor(private cdr: ChangeDetectorRef) {}

  private http = inject(HttpClient);
  private url = 'http://localhost:8080';

  result: (Book | Movie | Series)[] = [];
  selectedResult: Book | Movie | Series | null = null;

  protected readonly form = new FormGroup({
    search: new FormControl(''),
    tagFilter: new FormControl<string[]>([]),
    typeFilter: new FormControl<string[]>([]),
    rating: new FormControl([0, 10]),
    yearRange: new FormControl([1800, 2025]),
    duration: new FormControl([0, 300]),
    quantity: new FormControl([0, 2000]),
    seasons: new FormControl([0, 20]),
    author: new FormControl(''),
    director: new FormControl(''),
    actors: new FormControl(''),
    country: new FormControl(),
  });

  protected readonly tagFilters = [
    'Fantasy',
    'Science fiction',
    'Drama',
    'Hollywood',
    'Family',
  ];

  protected readonly typeFilters = ['Book', 'Film', 'Series'];

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

  search(): void {
    const title: string = this.form.get('search')?.getRawValue();
    const tags = this.form.get('tagFilter')?.getRawValue();
    const types = this.form.get('typeFilter')?.getRawValue();
    const rating = this.form.get('rating')?.getRawValue();
    const year = this.form.get('yearRange')?.getRawValue();
    const country = this.form.get('country')?.getRawValue();
    const author = this.form.get('author')?.getRawValue();
    const director = this.form.get('director')?.getRawValue();
    const actors = this.form.get('actors')?.getRawValue();
    const duration = this.form.get('duration')?.getRawValue();
    const quantity = this.form.get('quantity')?.getRawValue();
    const seasons = this.form.get('seasons')?.getRawValue();

    const generalBody = {
      title: title ? title : '',
      tags: tags
        ? tags.map((tag: string) =>
            tag.toUpperCase().replace(/\s+/g, '_')
          )
        : [],
      ratingFrom: rating[0],
      ratingTo: rating[1],
      startYearFrom: year[0],
      startYearTo: year[1],
      country: country,
    };

    const movieBody = {
      ...generalBody,
      durationFrom: duration[0],
      durationTo: duration[1],
      directors: [director],
      actors: [actors],
    };

    const bookBody = {
      ...generalBody,
      quantityPagesFrom: quantity[0],
      quantityPagesTo: quantity[1],
      authors: [author],
    };

    const seriesBody = {
      ...generalBody,
      seasonsFrom: seasons[0],
      seasonsTo: seasons[1],
      directors: [director],
      actors: [actors],
    };

    console.log(
      `Searching for "${title}" with tags: ${tags}; type: ${types};
      rating: ${rating}; year: ${year}; country: ${country};
      author: ${author}; director: ${director}; actors: ${actors};
      duration: ${duration}; quantity: ${quantity}; sesons: ${seasons}`
    );

    console.log(movieBody, bookBody, seriesBody);

    const tasks$ = [];

    if (!types.length || types.includes('Book')) {
      tasks$.push(
        this.http.post<Book[]>(this.url + '/books/search', bookBody, {
          params: { page: 0, size: 0 },
        })
      );
    }
    if (!types.length || types.includes('Film')) {
      tasks$.push(
        this.http.post<Movie[]>(
          this.url + '/movies/search',
          movieBody,
          { params: { page: 0, size: 0 } }
        )
      );
    }
    if (!types.length || types.includes('Series')) {
      tasks$.push(
        this.http.post<Series[]>(
          this.url + '/series/search',
          seriesBody,
          { params: { page: 0, size: 0 } }
        )
      );
    }

    this.result = [];
    zip(...tasks$).subscribe(results => {
      results.forEach(
        res => (this.result = [...this.result, ...res])
      );
      this.cdr.detectChanges();
    });
  }

  protected selectResult(res: Book | Movie | Series) {
    this.selectedResult = res;
  }

  protected backToList() {
    this.selectedResult = null;
  }

  protected handleUpdated(updated: Book | Movie | Series) {
    if (this.result) {
      this.result = this.result.map(item =>
        item.id === updated.id ? updated : item
      );
    }
  }
}
