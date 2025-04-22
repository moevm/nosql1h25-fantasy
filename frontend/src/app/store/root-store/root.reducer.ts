import { createReducer, on } from '@ngrx/store';
import { rootActions } from './root.actions';
import { Movie } from '../../data-access/movie.service';
import { Book } from '../../data-access/book.service';
import { Series } from '../../data-access/series.service';

export interface RootState {
  movies: Movie[];
  books: Book[];
  series: Series[];
  searchQuery: string;
}

export const initialState: RootState = {
  movies: [],
  books: [],
  series: [],
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
  on(rootActions.seriesFetched, (state, { content }) => ({
    ...state,
    series: content,
  })),
  on(rootActions.filteredCards, (state, { query }) => {
    return {
      ...state,
      searchQuery: query,
    };
  })
);
