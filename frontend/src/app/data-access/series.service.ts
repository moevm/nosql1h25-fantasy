import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Series {
  id: string;
  title: string;
  description: string;
  country: string;
  seasons: number;
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
export class SeriesService {
  private readonly apiUrl = 'http://localhost:8080/series/search';

  private http = inject(HttpClient);

  getSeries(): Observable<Series[]> {
    return this.http.post<Series[]>(
      `${this.apiUrl}?page=0&size=0`,
      {}
    );
  }
}
