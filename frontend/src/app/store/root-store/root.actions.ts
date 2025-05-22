import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { Movie } from '../../data-access/movie.service';
import { Book } from '../../data-access/book.service';
import { Series } from '../../data-access/series.service';

export const rootActions = createActionGroup({
  source: 'root-actions',
  events: {
    filteredCards: props<{ query: string }>(),
    moviesFetched: props<{ content: Movie[] }>(),
    moviePageChanged: props<{ page: number }>(),
    moviePageFetched: props<{
      content: Movie[];
      page: number;
      pageSize: number;
    }>(),
    movieInit: emptyProps(),
    booksFetched: props<{ content: Book[] }>(),
    bookInit: emptyProps(),
    seriesFetched: props<{ content: Series[] }>(),
    seriesPageChanged: props<{ page: number }>(),
    seriesPageFetched: props<{
      content: Series[];
      page: number;
      pageSize: number;
    }>(),
    seriesInit: emptyProps(),
    objectUpdated: props<{ updatedObject: Book | Series | Movie }>(),
  },
});
