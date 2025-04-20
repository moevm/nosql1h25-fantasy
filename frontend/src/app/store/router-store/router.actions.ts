import { createActionGroup, emptyProps } from '@ngrx/store';

export const routerActions = createActionGroup({
  source: 'router-actions',
  events: {
    navigateTo: emptyProps(),
    navigateToFilter: emptyProps(),
    navigateToMovies: emptyProps(),
  },
});
