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
  selectCurrentMoviePage,
  selectIsLastPage,
} from '../store/root-store/root.selectors';
import { TuiAvatar } from '@taiga-ui/kit';
import {
  TuiAppearance,
  TuiButton,
  TuiIcon,
  TuiTitle,
} from '@taiga-ui/core';
import { TuiCard } from '@taiga-ui/layout';
import { Movie } from '../data-access/movie.service';
import { DetailsComponent } from '../details/details.component';
import { DecimalPipe, NgIf } from '@angular/common';

@Component({
  selector: 'app-movies',
  imports: [
    TuiAvatar,
    TuiAppearance,
    TuiCard,
    TuiTitle,
    TuiIcon,
    DetailsComponent,
    NgIf,
    DecimalPipe,
    TuiButton,
  ],
  templateUrl: './movies.component.html',
  styleUrl: './movies.component.less',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
})
export class MoviesComponent implements OnInit {
  private store = inject(Store);

  selectedMovie: Movie | null = null;

  currentPage = this.store.selectSignal(selectCurrentMoviePage);
  isLastPage = this.store.selectSignal(selectIsLastPage);

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
    this.store.dispatch(rootActions.moviePageChanged({ page: 0 }));
  }

  protected selectMovie(movie: Movie) {
    this.selectedMovie = movie;
  }

  protected backToList() {
    this.selectedMovie = null;
  }

  nextPage() {
    this.store.dispatch(
      rootActions.moviePageChanged({ page: this.currentPage() + 1 })
    );
  }

  prevPage() {
    const current = this.currentPage();
    if (current > 0) {
      this.store.dispatch(
        rootActions.moviePageChanged({ page: current - 1 })
      );
    }
  }
}
