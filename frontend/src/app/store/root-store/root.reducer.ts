import { createReducer, on } from '@ngrx/store';
import { rootActions } from './root.actions';
import {
  Movie,
  MOVIE_PAGE_SIZE,
} from '../../data-access/movie.service';
import { Book, BOOK_PAGE_SIZE } from '../../data-access/book.service';
import {
  Series,
  SERIES_PAGE_SIZE,
} from '../../data-access/series.service';

export interface RootState {
  movies: Movie[];
  books: Book[];
  series: Series[];
  searchQuery: string;
  currentMoviePage: number;
  moviePageSize: number;
  currentSeriesPage: number;
  seriesPageSize: number;
  currentBookPage: number;
  bookPageSize: number;
}

export const initialState: RootState = {
  movies: [],
  books: [],
  series: [],
  searchQuery: '',
  currentMoviePage: 0,
  moviePageSize: MOVIE_PAGE_SIZE,
  currentSeriesPage: 0,
  seriesPageSize: SERIES_PAGE_SIZE,
  currentBookPage: 0,
  bookPageSize: BOOK_PAGE_SIZE,
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
  ),
  on(
    rootActions.seriesPageFetched,
    (state, { content, page, pageSize }) => ({
      ...state,
      series: content,
      currentSeriesPage: page,
      seriesPageSize: pageSize,
    })
  ),
  on(
    rootActions.bookPageFetched,
    (state, { content, page, pageSize }) => ({
      ...state,
      books: content,
      currentBookPage: page,
      bookPageSize: pageSize,
    })
  )
);
