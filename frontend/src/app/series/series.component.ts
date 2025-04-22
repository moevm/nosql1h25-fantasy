import { Component, computed, inject, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import {
  selectFilteredSeries,
  selectSearchQuery,
  selectSeries,
} from '../store/root-store/root.selectors';
import { rootActions } from '../store/root-store/root.actions';
import { TuiAvatar } from '@taiga-ui/kit';
import {
  TuiAppearance,
  TuiButton,
  TuiIcon,
  TuiTitle,
} from '@taiga-ui/core';
import { TuiCard } from '@taiga-ui/layout';

@Component({
  selector: 'app-series',
  imports: [
    TuiAvatar,
    TuiAppearance,
    TuiCard,
    TuiTitle,
    TuiIcon,
    TuiButton,
  ],
  templateUrl: './series.component.html',
  styleUrl: './series.component.less',
})
export class SeriesComponent implements OnInit {
  private store = inject(Store);

  protected filteredSeries = this.store.selectSignal(
    selectFilteredSeries
  );

  protected allSeries = this.store.selectSignal(selectSeries);

  protected searchQuery = this.store.selectSignal(selectSearchQuery);

  protected series = computed(() => {
    return this.filteredSeries().length === 0 &&
      this.searchQuery().length === 0
      ? this.allSeries()
      : this.filteredSeries();
  });

  ngOnInit() {
    this.store.dispatch(rootActions.seriesInit());
  }
}
