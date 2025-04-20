import { createReducer, on } from '@ngrx/store';
import { Movie } from '../../data-access/movie.service';
import { rootActions } from './root.actions';

export interface MovieState {
  movies: Movie[];
}

export const initialState: MovieState = {
  movies: [],
};

export const rootReducers = createReducer(
  initialState,
  on(rootActions.moviesFetched, (state, { content }) => ({
    ...state,
    movies: content,
  }))
);
