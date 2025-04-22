import { createReducer, on } from '@ngrx/store';
import { Movie } from '../../data-access/movie.service';
import { rootActions } from './root.actions';
import { Book } from '../../data-access/book.service';

export interface RootState {
  movies: Movie[];
  books: Book[];
  searchQuery: string;
}

export const initialState: RootState = {
  movies: [],
  books: [],
  searchQuery: '',
};

export const rootReducers = createReducer(
  initialState,
  on(rootActions.moviesFetched, (state, { content }) => ({
    ...state,
    movies: content,
  })),
  on(rootActions.booksFetched, (state, { content }) => ({
    ...state,
    books: content,
  })),
  on(rootActions.filteredCards, (state, { query }) => {
    return {
      ...state,
      searchQuery: query,
    };
  })
);
