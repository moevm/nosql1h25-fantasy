import {
  ChangeDetectorRef,
  Component,
  inject,
  OnInit,
} from '@angular/core';
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
import {
  TuiAvatar,
  TuiChevron,
  TuiFilter,
  TuiTooltip,
} from '@taiga-ui/kit';
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
    TuiTooltip,
  ],
  templateUrl: './filters.component.html',
  styleUrl: './filters.component.less',
  standalone: true,
})
export class FiltersComponent implements OnInit {
  constructor(private cdr: ChangeDetectorRef) {}

  private http = inject(HttpClient);
  private url = 'http://localhost:8080';

  books: Book[] = [];
  movies: Movie[] = [];
  series: Series[] = [];
  selectedResult: Book | Movie | Series | null = null;

  MAX_DURATION = 300;
  MAX_QUANTITY = 2000;
  MAX_SEASONS = 20;

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
    'Action',
    'Adventure',
    'Fantasy',
    'Horror',
    'Mystery',
    'Science fiction',
  ];

  protected readonly typeFilters = ['Book', 'Film', 'Series'];

  protected readonly items = [
    'France',
    'Germany',
    'Japan',
    'UK',
    'USA',
  ];

  protected disableAllNonCommonFields() {
    this.form.controls['duration'].disable();
    this.form.controls['quantity'].disable();
    this.form.controls['seasons'].disable();
    this.form.controls['author'].disable();
    this.form.controls['director'].disable();
    this.form.controls['actors'].disable();
  }

  ngOnInit() {
    this.books = [];
    this.movies = [];
    this.series = [];
    this.disableAllNonCommonFields();
    this.form.controls['typeFilter'].valueChanges.subscribe(value => {
      this.disableAllNonCommonFields();
      if (value?.length === 1) {
        if (value[0] === 'Book') {
          this.form.controls['quantity'].enable();
          this.form.controls['author'].enable();
        } else if (value[0] === 'Film')
          this.form.controls['duration'].enable();
        else if (value[0] === 'Series')
          this.form.controls['seasons'].enable();
      }
      if (!this.isTypeSelected('Book')) {
        this.form.controls['director'].enable();
        this.form.controls['actors'].enable();
      }
    });
  }

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
      country: country === null ? null : country.toUpperCase(),
    };

    const movieBody = {
      ...generalBody,
      durationFrom: this.form.controls['duration'].enabled
        ? duration[0]
        : 0,
      durationTo: this.form.controls['duration'].enabled
        ? duration[1]
        : this.MAX_DURATION,
      directors: this.form.controls['director'].enabled
        ? [director]
        : [],
      actors: this.form.controls['actors'].enabled ? [actors] : [],
    };

    const bookBody = {
      ...generalBody,
      quantityPagesFrom: this.form.controls['quantity'].enabled
        ? quantity[0]
        : 0,
      quantityPagesTo: this.form.controls['quantity'].enabled
        ? quantity[1]
        : this.MAX_QUANTITY,
      authors: this.form.controls['author'].enabled ? [author] : [],
    };

    const seriesBody = {
      ...generalBody,
      seasonsFrom: this.form.controls['seasons'].enabled
        ? seasons[0]
        : 0,
      seasonsTo: this.form.controls['seasons'].enabled
        ? seasons[1]
        : this.MAX_SEASONS,
      directors: this.form.controls['director'].enabled
        ? [director]
        : [],
      actors: this.form.controls['actors'].enabled ? [actors] : [],
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

    this.books = [];
    this.movies = [];
    this.series = [];
    zip(...tasks$).subscribe(results => {
      let i = 0;
      if (!types.length || types.includes('Book'))
        this.books = results[i++] as Book[];
      if (!types.length || types.includes('Film'))
        this.movies = results[i++] as Movie[];
      if (!types.length || types.includes('Series'))
        this.series = results[i++] as Series[];
      this.cdr.detectChanges();
    });
  }

  protected isTypeSelected(type: string): boolean {
    const selectedTypes = this.form.getRawValue().typeFilter;
    return !selectedTypes?.length || selectedTypes.includes(type);
  }

  protected selectResult(res: Book | Movie | Series) {
    this.selectedResult = res;
  }

  protected backToList() {
    this.selectedResult = null;
  }

  protected handleUpdated(updated: Book | Movie | Series) {
    if (updated.type === 'BOOK' && this.books) {
      this.books = this.books.map(item =>
        item.id === updated.id ? (updated as Book) : item
      );
    }
    if (updated.type === 'FILM' && this.movies) {
      this.movies = this.movies.map(item =>
        item.id === updated.id ? (updated as Movie) : item
      );
    }
    if (updated.type === 'SERIES' && this.series) {
      this.series = this.series.map(item =>
        item.id === updated.id ? (updated as Series) : item
      );
    }
  }
}
