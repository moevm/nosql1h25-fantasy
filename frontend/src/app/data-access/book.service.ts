import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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
  private readonly apiUrl = 'http://localhost:8080/books';

  private http = inject(HttpClient);

  getBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.apiUrl}?page=0&size=0`);
  }
}
