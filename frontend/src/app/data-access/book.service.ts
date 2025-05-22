import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export const BOOK_PAGE_SIZE = 10;

export interface Book {
  id: string;
  title: string;
  description: string;
  country: string;
  quantityPages: number;
  startYear: number;
  endYear: number;
  rating: number;
  tags: string[];
  type: string;
  persons: { name: string; role: string }[];
  reviews: { rating: number; reviewerName: string; text: string }[];
}

@Injectable({
  providedIn: 'root',
})
export class BookService {
  private readonly apiUrl = 'http://localhost:8080/books/search';

  private http = inject(HttpClient);

  getBooks(page = 0, size = 0): Observable<Book[]> {
    return this.http.post<Book[]>(
      `${this.apiUrl}?page=${page}&size=${size}`,
      {}
    );
  }
}
