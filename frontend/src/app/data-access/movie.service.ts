import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Movie {
  id: string;
  title: string;
  description: string;
  country: string;
  duration: number;
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
export class MovieService {
  private readonly apiUrl = 'http://localhost:8080/movies/search';

  private http = inject(HttpClient);

  getMovies(page = 0, size = 0): Observable<Movie[]> {
    return this.http.post<Movie[]>(
      `${this.apiUrl}?page=${page}&size=${size}`,
      {}
    );
  }
}
