import {
  ChangeDetectionStrategy,
  Component,
  inject,
  OnInit,
} from '@angular/core';
import { Store } from '@ngrx/store';
import { rootActions } from '../store/root-store/root.actions';
import { selectMovies } from '../store/root-store/root.selectors';
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

  protected movies = this.store.selectSignal(selectMovies);

  ngOnInit() {
    this.store.dispatch(rootActions.movieInit());
  }
}
