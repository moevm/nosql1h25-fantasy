import { createReducer, on } from '@ngrx/store';
import { Movie } from '../../data-access/movie.service';
import { rootActions } from './root.actions';

export interface MovieState {
  movies: Movie[];
  searchQuery: string;
}

export const initialState: MovieState = {
  movies: [],
  searchQuery: '',
};

export const rootReducers = createReducer(
  initialState,
  on(rootActions.moviesFetched, (state, { content }) => ({
    ...state,
    movies: content,
  })),
  on(rootActions.filteredCards, (state, { query }) => {
    return {
      ...state,
      searchQuery: query,
    };
  })
);
