import * as routerEffects from './router.effects';
import { provideEffects } from '@ngrx/effects';

export const routerEffectsProvider = provideEffects(routerEffects);
