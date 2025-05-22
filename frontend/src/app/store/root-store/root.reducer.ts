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
  currentMoviePage: number;
  moviePageSize: number;
}

export const initialState: RootState = {
  movies: [],
  books: [],
  series: [],
  searchQuery: '',
  currentMoviePage: 0,
  moviePageSize: 10,
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
  }),
  on(rootActions.objectUpdated, (state, { updatedObject }) => {
    if (updatedObject.type === 'FILM') {
      return {
        ...state,
        movies: state.movies.map(movie =>
          movie.id === updatedObject.id
            ? (updatedObject as Movie)
            : movie
        ),
      };
    }

    if (updatedObject.type === 'BOOK') {
      return {
        ...state,
        books: state.books.map(book =>
          book.id === updatedObject.id
            ? (updatedObject as Book)
            : book
        ),
      };
    }

    if (updatedObject.type === 'SERIES') {
      return {
        ...state,
        series: state.series.map(series =>
          series.id === updatedObject.id
            ? (updatedObject as Series)
            : series
        ),
      };
    }

    return state;
  }),
  on(rootActions.moviePageChanged, (state, { page }) => ({
    ...state,
    currentMoviePage: page,
  })),
  on(
    rootActions.moviePageFetched,
    (state, { content, page, pageSize }) => ({
      ...state,
      movies: content,
      currentMoviePage: page,
      moviePageSize: pageSize,
    })
  )
);
