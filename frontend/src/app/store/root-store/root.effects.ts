import { Actions, createEffect, ofType } from '@ngrx/effects';
import { inject } from '@angular/core';
import { rootActions } from './root.actions';
import { switchMap } from 'rxjs';
import { map } from 'rxjs/operators';
import { MOVIE_PAGE_SIZE, MovieService } from '../../data-access/movie.service';
import { BookService } from '../../data-access/book.service';
import {
  SERIES_PAGE_SIZE,
  SeriesService,
} from '../../data-access/series.service';

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

export const seriesInit$ = createEffect(
  (
    actions$ = inject(Actions),
    seriesService = inject(SeriesService)
  ) => {
    return actions$.pipe(
      ofType(rootActions.seriesInit),
      switchMap(() =>
        seriesService
          .getSeries()
          .pipe(
            map(response =>
              rootActions.seriesFetched({ content: response })
            )
          )
      )
    );
  },
  { functional: true }
);

export const moviePageChanged$ = createEffect(
  (
    actions$ = inject(Actions),
    movieService = inject(MovieService)
  ) => {
    return actions$.pipe(
      ofType(rootActions.moviePageChanged),
      switchMap(({ page }) =>
        movieService.getMovies(page, MOVIE_PAGE_SIZE).pipe(
          map(movies =>
            rootActions.moviePageFetched({
              content: movies,
              page,
              pageSize: MOVIE_PAGE_SIZE,
            })
          )
        )
      )
    );
  },
  { functional: true }
);

export const seriesPageChanged$ = createEffect(
  (
    actions$ = inject(Actions),
    movieService = inject(SeriesService)
  ) => {
    return actions$.pipe(
      ofType(rootActions.seriesPageChanged),
      switchMap(({ page }) =>
        movieService.getSeries(page, SERIES_PAGE_SIZE).pipe(
          map(series =>
            rootActions.seriesPageFetched({
              content: series,
              page,
              pageSize: SERIES_PAGE_SIZE,
            })
          )
        )
      )
    );
  },
  { functional: true }
);
