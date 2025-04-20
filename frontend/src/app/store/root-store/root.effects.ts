import { Actions, createEffect, ofType } from '@ngrx/effects';
import { inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { rootActions } from './root.actions';
import { switchMap, tap } from 'rxjs';
import { MovieService } from '../../data-access/movie.service';

export const moviesInit$ = createEffect(
  (
    actions$ = inject(Actions),
    store = inject(Store),
    movieService = inject(MovieService)
  ) => {
    return actions$.pipe(
      ofType(rootActions.movieInit),
      switchMap(() =>
        movieService
          .getMovies()
          .pipe(
            tap(response =>
              store.dispatch(
                rootActions.moviesFetched({ content: response })
              )
            )
          )
      )
    );
  },
  { functional: true, dispatch: false }
);
