import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'filter',
    loadComponent: () =>
      import('./filters/filters.component').then(
        m => m.FiltersComponent
      ),
  },
  {
    path: 'movies',
    loadComponent: () =>
      import('./movies/movies.component').then(
        m => m.MoviesComponent
      ),
  },
  {
    path: 'books',
    loadComponent: () =>
      import('./books/books.component').then(
        m => m.BooksComponent
      ),
  },
  {
    path: 'series',
    loadComponent: () =>
      import('./series/series.component').then(
        m => m.SeriesComponent
      ),
  },
  { path: '', redirectTo: 'movies', pathMatch: 'full' },
];
