import { Component, computed, inject, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import {
  selectCurrentSeriesPage,
  selectFilteredSeries,
  selectIsLastSeriesPage,
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
import { Series } from '../data-access/series.service';
import { DetailsComponent } from '../details/details.component';
import { DecimalPipe, NgIf } from '@angular/common';

@Component({
  selector: 'app-series',
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
  templateUrl: './series.component.html',
  styleUrl: './series.component.less',
  standalone: true,
})
export class SeriesComponent implements OnInit {
  private store = inject(Store);

  selectedShow: Series | null = null;

  currentPage = this.store.selectSignal(selectCurrentSeriesPage);
  isLastPage = this.store.selectSignal(selectIsLastSeriesPage);

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
    this.store.dispatch(rootActions.seriesPageChanged({ page: 0 }));
  }

  protected selectShow(show: Series) {
    this.selectedShow = show;
  }

  protected backToList() {
    this.selectedShow = null;
  }

  nextPage() {
    this.store.dispatch(
      rootActions.seriesPageChanged({ page: this.currentPage() + 1 })
    );
  }

  prevPage() {
    const current = this.currentPage();
    if (current > 0) {
      this.store.dispatch(
        rootActions.seriesPageChanged({ page: current - 1 })
      );
    }
  }
}
