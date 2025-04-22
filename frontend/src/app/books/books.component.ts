import { Component, computed, inject, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import {
  selectFilteredBooks,
  selectBooks,
  selectSearchQuery,
} from '../store/root-store/root.selectors';
import { rootActions } from '../store/root-store/root.actions';
import { TuiAvatar } from '@taiga-ui/kit';
import { TuiAppearance, TuiButton, TuiIcon, TuiTitle } from '@taiga-ui/core';
import { TuiCard } from '@taiga-ui/layout';

@Component({
  selector: 'app-books',
  imports: [
    TuiAvatar,
    TuiAppearance,
    TuiCard,
    TuiTitle,
    TuiIcon,
    TuiButton,
  ],
  templateUrl: './books.component.html',
  styleUrl: './books.component.less',
})
export class BooksComponent implements OnInit {
  private store = inject(Store);

  protected filteredBooks = this.store.selectSignal(
    selectFilteredBooks
  );

  protected allBooks = this.store.selectSignal(selectBooks);

  protected searchQuery = this.store.selectSignal(selectSearchQuery);

  protected books = computed(() => {
    return this.filteredBooks().length === 0 &&
      this.searchQuery().length === 0
      ? this.allBooks()
      : this.filteredBooks();
  });

  ngOnInit() {
    this.store.dispatch(rootActions.bookInit());
  }
}
