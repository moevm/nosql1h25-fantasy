import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./movies/movies.component').then(
        m => m.MoviesComponent
      ),
  },
  {
    path: 'filter',
    loadComponent: () =>
      import('./filters/filters.component').then(
        m => m.FiltersComponent
      ),
  },
  {
    path: 'books',
    loadComponent: () =>
      import('./books/books.component').then(
        m => m.BooksComponent
      ),
  },
];
