import { Actions, createEffect, ofType } from '@ngrx/effects';
import { inject } from '@angular/core';
import { rootActions } from './root.actions';
import { switchMap } from 'rxjs';
import { MovieService } from '../../data-access/movie.service';
import { map } from 'rxjs/operators';

export const moviesInit$ = createEffect(
  (
    actions$ = inject(Actions),
    movieService = inject(MovieService)
  ) => {
    return actions$.pipe(
      ofType(rootActions.movieInit),
      switchMap(() =>
        movieService
          .getMovies()
          .pipe(
            map(response =>
              rootActions.moviesFetched({ content: response })
            )
          )
      )
    );
  },
  { functional: true }
);
