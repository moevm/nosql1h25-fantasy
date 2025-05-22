import { createFeatureSelector, createSelector } from '@ngrx/store';
import { RootState } from './root.reducer';

export const selectRootState =
  createFeatureSelector<RootState>('rootStore');

export const selectSearchQuery = createSelector(
  selectRootState,
  state => state.searchQuery
);

export const selectMovies = createSelector(
  selectRootState,
  state => state.movies
);

export const selectFilteredMovies = createSelector(
  selectMovies,
  selectSearchQuery,
  (movies, query) => {
    if (!query.trim()) {
      return movies;
    }

    const lowerQuery = query.toLowerCase();
    return movies.filter(movie =>
      movie.title.toLowerCase().includes(lowerQuery)
    );
  }
);

export const selectBooks = createSelector(
  selectRootState,
  state => state.books
);

export const selectFilteredBooks = createSelector(
  selectBooks,
  selectSearchQuery,
  (books, query) => {
    if (!query.trim()) {
      return books;
    }

    const lowerQuery = query.toLowerCase();
    return books.filter(book =>
      book.title.toLowerCase().includes(lowerQuery)
    );
  }
);

export const selectSeries = createSelector(
  selectRootState,
  state => state.series
);

export const selectFilteredSeries = createSelector(
  selectSeries,
  selectSearchQuery,
  (series, query) => {
    if (!query.trim()) {
      return series;
    }

    const lowerQuery = query.toLowerCase();
    return series.filter(show =>
      show.title.toLowerCase().includes(lowerQuery)
    );
  }
);

export const selectCurrentMoviePage = createSelector(
  selectRootState,
  (state: RootState) => state.currentMoviePage
);

export const selectIsLastMoviePage = createSelector(
  selectRootState,
  state => state.movies.length < state.moviePageSize
);

export const selectCurrentSeriesPage = createSelector(
  selectRootState,
  (state: RootState) => state.currentSeriesPage
);

export const selectIsLastSeriesPage = createSelector(
  selectRootState,
  state => state.series.length < state.seriesPageSize
);
