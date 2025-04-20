import { createFeatureSelector, createSelector } from '@ngrx/store';
import { MovieState } from './root.reducer';

export const selectRootState =
  createFeatureSelector<MovieState>('rootStore');

export const selectMovies = createSelector(
  selectRootState,
  state => state.movies
);
