import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { Movie } from '../../data-access/movie.service';
import { Book } from '../../data-access/book.service';

export const rootActions = createActionGroup({
  source: 'root-actions',
  events: {
    moviesFetched: props<{ content: Movie[] }>(),
    movieInit: emptyProps(),
    filteredCards: props<{ query: string }>(),
    booksFetched: props<{ content: Book[] }>(),
    bookInit: emptyProps(),
  },
});
