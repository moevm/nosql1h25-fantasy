import { Actions, createEffect, ofType } from '@ngrx/effects';
import { inject } from '@angular/core';
import { rootActions } from './root.actions';
import { switchMap } from 'rxjs';
import { MovieService } from '../../data-access/movie.service';
import { map } from 'rxjs/operators';
import { BookService } from '../../data-access/book.service';

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

export const booksInit$ = createEffect(
  (
    actions$ = inject(Actions),
    booksService = inject(BookService)
  ) => {
    return actions$.pipe(
      ofType(rootActions.bookInit),
      switchMap(() =>
        booksService
          .getBooks()
          .pipe(
            map(response =>
              rootActions.booksFetched({ content: response })
            )
          )
      )
    );
  },
  { functional: true }
);
