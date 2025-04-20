import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { Movie } from '../../data-access/movie.service';

export const rootActions = createActionGroup({
  source: 'root-actions',
  events: {
    moviesFetched: props<{ content: Movie[] }>(),
    movieInit: emptyProps(),
    filteredCards: props<{ query: string }>(),
  },
});
