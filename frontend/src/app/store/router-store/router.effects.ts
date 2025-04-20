import { Actions, createEffect, ofType } from '@ngrx/effects';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { routerActions } from './router.actions';
import { tap } from 'rxjs';

export const navigateToFilter$ = createEffect(
  (actions$ = inject(Actions), router = inject(Router)) => {
    return actions$.pipe(
      ofType(routerActions.navigateToFilter),
      tap(() => router.navigate(['/filter']))
    );
  },
  { functional: true, dispatch: false }
);
