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
import { TuiAppearance, TuiIcon, TuiTitle } from '@taiga-ui/core';
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
  ],
  templateUrl: './movies.component.html',
  styleUrl: './movies.component.less',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
})
export class MoviesComponent implements OnInit {
  private store = inject(Store);

  selectedMovie: Movie | null = null;

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

  protected selectMovie(movie: Movie) {
    this.selectedMovie = movie;
  }

  protected backToList() {
    this.selectedMovie = null;
  }
}
