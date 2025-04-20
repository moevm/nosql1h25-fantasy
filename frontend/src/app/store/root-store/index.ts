import * as rootEffect from './root.effects';
import { rootReducers } from './root.reducer';
import { provideEffects } from '@ngrx/effects';
import { provideStore } from '@ngrx/store';

export const rootEffectProvider = provideEffects(rootEffect);
export const rootReducerProvider = provideStore({
  rootStore: rootReducers,
});
