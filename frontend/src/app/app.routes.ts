import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'filter',
    loadComponent: () =>
      import('./filters/filters.component').then(
        m => m.FiltersComponent
      ),
  },
];
