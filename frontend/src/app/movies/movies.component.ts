import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  OnInit,
} from '@angular/core';
import { Store } from '@ngrx/store';
import { rootActions } from '../store/root-store/root.actions';
import {
  selectFilteredMovies,
  selectMovies,
  selectSearchQuery,
} from '../store/root-store/root.selectors';
import { TuiAvatar } from '@taiga-ui/kit';
import {
  TuiAppearance,
  TuiButton,
  TuiIcon,
  TuiTitle,
} from '@taiga-ui/core';
import { TuiCard } from '@taiga-ui/layout';

@Component({
  selector: 'app-movies',
  imports: [
    TuiAvatar,
    TuiAppearance,
    TuiCard,
    TuiTitle,
    TuiIcon,
    TuiButton,
  ],
  templateUrl: './movies.component.html',
  styleUrl: './movies.component.less',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MoviesComponent implements OnInit {
  private store = inject(Store);

  protected filteredMovies = this.store.selectSignal(
    selectFilteredMovies
  );

  protected allMovies = this.store.selectSignal(selectMovies);

  protected searchQuery = this.store.selectSignal(selectSearchQuery);

  protected movies = computed(() => {
    return this.filteredMovies().length === 0 &&
      this.searchQuery().length === 0
      ? this.allMovies()
      : this.filteredMovies();
  });

  ngOnInit() {
    this.store.dispatch(rootActions.movieInit());
  }
}
