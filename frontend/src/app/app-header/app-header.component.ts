import {
  ChangeDetectionStrategy,
  Component,
  inject,
} from '@angular/core';
import { TuiNavigation } from '@taiga-ui/layout';
import { TuiButton, TuiIcon, TuiTextfield } from '@taiga-ui/core';
import {
  TuiInputModule,
  TuiTextfieldControllerModule,
} from '@taiga-ui/legacy';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Store } from '@ngrx/store';
import { routerActions } from '../store/router-store/router.actions';
import { rootActions } from '../store/root-store/root.actions';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    TuiNavigation,
    TuiButton,
    TuiInputModule,
    ReactiveFormsModule,
    TuiTextfield,
    TuiTextfieldControllerModule,
    TuiIcon,
    FormsModule,
  ],
  selector: 'app-header',
  styleUrl: './app-header.component.less',
  templateUrl: './app-header.component.html',
  standalone: true,
})
export class AppHeaderComponent {
  private store = inject(Store);

  protected searchQuery = '';

  protected goToFilters(): void {
    this.store.dispatch(routerActions.navigateToFilter());
  }

  protected navigateToMovies(): void {
    this.store.dispatch(routerActions.navigateToMovies());
  }

  protected navigateToBooks(): void {
    this.store.dispatch(routerActions.navigateToBooks());
  }

  protected navigateToSeries(): void {
    this.store.dispatch(routerActions.navigateToSeries());
  }

  protected onSearchChange(query: string) {
    this.store.dispatch(rootActions.filteredCards({ query }));
  }
}
