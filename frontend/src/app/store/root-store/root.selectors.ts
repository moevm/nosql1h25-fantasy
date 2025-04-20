import { createFeatureSelector, createSelector } from '@ngrx/store';
import { MovieState } from './root.reducer';

export const selectRootState =
  createFeatureSelector<MovieState>('rootStore');

export const selectMovies = createSelector(
  selectRootState,
  state => state.movies
);

export const selectSearchQuery = createSelector(
  selectRootState,
  state => state.searchQuery
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
