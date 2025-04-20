import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

export interface Movie {
  id: number;
  title: string;
  director: string;
  year: number;
  genre: string;
}

@Injectable({
  providedIn: 'root',
})
export class MovieService {
  private readonly apiUrl = 'http://localhost:8080/movies';

  private http = inject(HttpClient);

  getMovies(): Observable<Movie[]> {
    return this.http
      .get<{ content: Movie[] }>(`${this.apiUrl}?page=0&size=0`)
      .pipe(
        map(response => response.content),
        catchError(error => {
          console.error('Error fetching movies:', error);
          throw error;
        })
      );
  }
}
