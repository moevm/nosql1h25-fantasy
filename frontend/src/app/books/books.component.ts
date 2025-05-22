import { Component, computed, inject, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import {
  selectFilteredBooks,
  selectBooks,
  selectSearchQuery,
  selectCurrentBookPage,
  selectIsLastBookPage,
} from '../store/root-store/root.selectors';
import { rootActions } from '../store/root-store/root.actions';
import { TuiAvatar } from '@taiga-ui/kit';
import { TuiAppearance, TuiButton, TuiIcon, TuiTitle } from '@taiga-ui/core';
import { TuiCard } from '@taiga-ui/layout';
import { Book } from '../data-access/book.service';
import { DetailsComponent } from '../details/details.component';
import { DecimalPipe, NgIf } from '@angular/common';

@Component({
  selector: 'app-books',
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
  templateUrl: './books.component.html',
  styleUrl: './books.component.less',
  standalone: true,
})
export class BooksComponent implements OnInit {
  private store = inject(Store);

  selectedBook: Book | null = null;

  currentPage = this.store.selectSignal(selectCurrentBookPage);
  isLastPage = this.store.selectSignal(selectIsLastBookPage);

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
    this.store.dispatch(rootActions.bookPageChanged({ page: 0 }));
  }

  protected selectBook(book: Book) {
    this.selectedBook = book;
  }

  protected backToList() {
    this.selectedBook = null;
  }

  nextPage() {
    this.store.dispatch(
      rootActions.bookPageChanged({ page: this.currentPage() + 1 })
    );
  }

  prevPage() {
    const current = this.currentPage();
    if (current > 0) {
      this.store.dispatch(
        rootActions.bookPageChanged({ page: current - 1 })
      );
    }
  }
}
